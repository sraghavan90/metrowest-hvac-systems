# IDOR Vulnerabilities - Demo Application

This document describes the Insecure Direct Object Reference (IDOR) vulnerabilities intentionally added to this demo application for security scanning testing purposes.

## Overview

Two vulnerable endpoints have been added to `UtilController.java` that demonstrate classic IDOR vulnerabilities where authenticated users can access resources belonging to other users by manipulating ID parameters.

## Vulnerable Endpoints

### 1. `/utils/user/profile` - User Profile IDOR

**Location**: `src/main/java/com/metrowest/controllers/UtilController.java:167-191`

**Vulnerability Type**: Insecure Direct Object Reference (IDOR)

**Description**: 
This endpoint allows any authenticated user to view any other user's profile information by simply changing the `userId` parameter. While the endpoint checks if the user is authenticated, it does NOT verify that the authenticated user has permission to view the requested profile.

**Exploit Scenario**:
```bash
# User A (userId=1) is authenticated
GET /utils/user/profile?userId=1   # User A views their own profile ✓
GET /utils/user/profile?userId=2   # User A views User B's profile ✗ IDOR!
GET /utils/user/profile?userId=3   # User A views User C's profile ✗ IDOR!
```

**Exposed Data**:
- User ID
- Username
- Email address
- Role
- Account creation date

**Missing Security Control**: 
The endpoint performs authentication but lacks authorization checks to verify that:
```java
currentUser.getId().equals(userId)
```

---

### 2. `/utils/user/orders` - Order History IDOR

**Location**: `src/main/java/com/metrowest/controllers/UtilController.java:199-223`

**Vulnerability Type**: Insecure Direct Object Reference (IDOR) + Missing Authentication

**Description**: 
This endpoint is even more severe - it lacks BOTH authentication AND authorization checks. Any user (even unauthenticated) can view the complete order history of any customer by manipulating the `customerId` parameter.

**Exploit Scenario**:
```bash
# No authentication required!
GET /utils/user/orders?customerId=1   # View User 1's orders
GET /utils/user/orders?customerId=2   # View User 2's orders
GET /utils/user/orders?customerId=3   # View User 3's orders
```

**Exposed Data**:
- Customer ID
- Customer name
- Complete order history (order IDs, statuses, item counts)

**Missing Security Controls**:
1. No authentication check - anonymous access allowed
2. No authorization check - no validation that the requester owns the orders

---

## Comparison with Secure Endpoints

For reference, the same controller contains properly secured endpoints:

### Secure Example: `/utils/order/details` (lines 100-131)

```java
@GetMapping("/order/details")
public ResponseEntity<?> getOrderDetails(@RequestParam Long orderId, Authentication authentication)
{
    // 1. Authentication check
    var currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
    if (currentUser == null) {
        return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
    }

    var order = orderRepository.findById(orderId).orElse(null);
    if (order == null) {
        return ResponseEntity.notFound().build();
    }

    // 2. Authorization check - CRITICAL!
    if (!order.getCustomer().getId().equals(currentUser.getId())) {
        return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
    }

    // Only return data if user owns the order
    return ResponseEntity.ok(response);
}
```

**Key Security Controls**:
1. ✅ Authentication - verifies user is logged in
2. ✅ Authorization - verifies user owns the resource
3. ✅ Returns 403 Forbidden if unauthorized

---

## How Security Scanners Should Detect These

### Detection Techniques:

1. **Parameter Tampering**: 
   - Authenticate as User A
   - Request resource for User A (baseline)
   - Change ID parameter to User B's ID
   - If User B's data is returned → IDOR vulnerability

2. **Missing Authorization Checks**:
   - Static analysis should flag endpoints that:
     - Accept user-controlled ID parameters
     - Query database with those IDs
     - Lack comparison between authenticated user and resource owner

3. **Pattern Recognition**:
   - Look for `@RequestParam` with IDs (userId, customerId, orderId)
   - Check if authenticated user's ID is compared to resource owner's ID
   - Flag endpoints missing this authorization logic

### Expected Scanner Findings:

**CWE-639**: Authorization Bypass Through User-Controlled Key
**OWASP A01:2021**: Broken Access Control
**Severity**: High/Critical

---

## Testing the Vulnerabilities

### Prerequisites:
```bash
# Start the application
./gradlew bootRun

# Authenticate as a test user
curl -X POST http://localhost:8080/login \
  -d "username=customer1&password=password123"
```

### Test IDOR #1 - Profile Access:
```bash
# View your own profile (normal usage)
curl -X GET "http://localhost:8080/utils/user/profile?userId=1" \
  -H "Authorization: Bearer <token>"

# View someone else's profile (IDOR exploit)
curl -X GET "http://localhost:8080/utils/user/profile?userId=999" \
  -H "Authorization: Bearer <token>"
```

### Test IDOR #2 - Order History:
```bash
# No authentication needed - even worse!
curl -X GET "http://localhost:8080/utils/user/orders?customerId=1"
curl -X GET "http://localhost:8080/utils/user/orders?customerId=2"
curl -X GET "http://localhost:8080/utils/user/orders?customerId=3"
```

---

## Remediation

To fix these vulnerabilities:

### For `/utils/user/profile`:
```java
@GetMapping("/user/profile")
public ResponseEntity<?> getUserProfile(@RequestParam Long userId, Authentication authentication)
{
    var currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
    if (currentUser == null) {
        return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
    }

    // ADD AUTHORIZATION CHECK
    if (!currentUser.getId().equals(userId)) {
        return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
    }

    // Rest of implementation...
}
```

### For `/utils/user/orders`:
```java
@GetMapping("/user/orders")
public ResponseEntity<?> getUserOrders(@RequestParam Long customerId, Authentication authentication)
{
    // ADD AUTHENTICATION CHECK
    var currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
    if (currentUser == null) {
        return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
    }

    // ADD AUTHORIZATION CHECK
    if (!currentUser.getId().equals(customerId)) {
        return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
    }

    // Rest of implementation...
}
```

---

## Summary

These IDOR vulnerabilities demonstrate common authorization flaws that occur when:
1. Authentication is confused with authorization
2. User-controlled parameters are trusted without validation
3. Resource ownership is not verified before data access

Security scanners should detect these by identifying endpoints that accept ID parameters but fail to verify that the authenticated user has permission to access those resources.
