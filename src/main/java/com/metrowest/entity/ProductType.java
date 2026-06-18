package com.metrowest.entity;

public enum ProductType
{
    SERVICE,
    EQUIPMENT,
    FASTENER,
    FILTER,
    ELECTRONIC,
    CHEMICAL,
    DUCT,

    ;

    /// Decodes a [String] representation of a user role into a [ProductType] value.
    ///
    /// @return the [ProductType] value that matches the provided `role_string`, or `null` if the value is invalid
    public static ProductType from_string(String role_string)
    {
        try { return ProductType.valueOf(role_string.toUpperCase()); }
        catch (IllegalArgumentException e) { return null; }
    }
}
