package com.metrowest.controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Controller
public class VulnerableSearchController
{
    @PersistenceContext
    private EntityManager entityManager;

    // HIGH SEVERITY: SQL Injection vulnerability
    @GetMapping("/admin/search")
    public String searchUsers(@RequestParam("query") String query, Model model)
    {
        try
        {
            // Direct SQL injection vulnerability - no parameterized query
            String sql = "SELECT * FROM users WHERE username LIKE '%" + query + "%'";
            var results = entityManager.createNativeQuery(sql).getResultList();
            model.addAttribute("results", results);
        }
        catch (Exception e)
        {
            model.addAttribute("error", e.getMessage());
        }
        return "admin/search_results";
    }

    // HIGH SEVERITY: Command Injection vulnerability
    @PostMapping("/admin/backup")
    public String backupDatabase(@RequestParam("filename") String filename, Model model)
    {
        try
        {
            // Command injection vulnerability - unsanitized user input in system command
            String command = "mysqldump -u root -p database > /backups/" + filename;
            Runtime.getRuntime().exec(command);
            model.addAttribute("message", "Backup created: " + filename);
        }
        catch (IOException e)
        {
            model.addAttribute("error", "Backup failed: " + e.getMessage());
        }
        return "admin/backup_result";
    }

    // HIGH SEVERITY: Path Traversal vulnerability
    @GetMapping("/admin/logs")
    public String viewLogs(@RequestParam("file") String filename, Model model)
    {
        try
        {
            // Path traversal vulnerability - no validation of file path
            File logFile = new File("/var/logs/" + filename);
            BufferedReader reader = new BufferedReader(new FileReader(logFile));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                content.append(line).append("\n");
            }
            reader.close();
            model.addAttribute("logContent", content.toString());
        }
        catch (IOException e)
        {
            model.addAttribute("error", "Could not read log file: " + e.getMessage());
        }
        return "admin/log_viewer";
    }

    // HIGH SEVERITY: Hardcoded credentials
    @GetMapping("/admin/db_status")
    public String checkDatabaseStatus(Model model)
    {
        try
        {
            // Hardcoded database credentials
            String dbUrl = "jdbc:mysql://localhost:3306/metrowest";
            String username = "admin";
            String password = "SuperSecret123!"; // Hardcoded password

            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");

            if (rs.next())
            {
                model.addAttribute("userCount", rs.getInt(1));
            }

            rs.close();
            stmt.close();
            conn.close();
        }
        catch (Exception e)
        {
            model.addAttribute("error", "Database check failed: " + e.getMessage());
        }
        return "admin/db_status";
    }

    // HIGH SEVERITY: XSS (Cross-Site Scripting) vulnerability
    @PostMapping("/admin/announcement")
    public String createAnnouncement(@RequestParam("message") String message, Model model)
    {
        // No HTML encoding - XSS vulnerability
        model.addAttribute("announcement", message);
        model.addAttribute("rawMessage", message); // Directly outputting user input
        return "admin/announcement";
    }

    // HIGH SEVERITY: Insecure deserialization
    @PostMapping("/admin/import_config")
    public String importConfig(@RequestParam("data") String serializedData, Model model)
    {
        try
        {
            // Unsafe deserialization of user-controlled data
            byte[] data = java.util.Base64.getDecoder().decode(serializedData);
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(
                new java.io.ByteArrayInputStream(data)
            );
            Object config = ois.readObject();
            ois.close();

            model.addAttribute("message", "Configuration imported successfully");
        }
        catch (Exception e)
        {
            model.addAttribute("error", "Import failed: " + e.getMessage());
        }
        return "admin/import_result";
    }

    // HIGH SEVERITY: LDAP Injection vulnerability
    @GetMapping("/admin/ldap_search")
    public String ldapSearch(@RequestParam("username") String username, Model model)
    {
        try
        {
            // LDAP injection - unsanitized user input in LDAP filter
            String filter = "(uid=" + username + ")";
            // javax.naming.directory.SearchControls would be used here
            model.addAttribute("ldapFilter", filter);
            model.addAttribute("message", "LDAP search completed");
        }
        catch (Exception e)
        {
            model.addAttribute("error", "LDAP search failed: " + e.getMessage());
        }
        return "admin/ldap_results";
    }

    // HIGH SEVERITY: XXE (XML External Entity) vulnerability
    @PostMapping("/admin/upload_xml")
    public String uploadXml(@RequestParam("xmlContent") String xmlContent, Model model)
    {
        try
        {
            // XXE vulnerability - no protection against external entity injection
            javax.xml.parsers.DocumentBuilderFactory factory =
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            // Missing: factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

            javax.xml.parsers.DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(
                new java.io.ByteArrayInputStream(xmlContent.getBytes())
            );

            model.addAttribute("message", "XML processed successfully");
        }
        catch (Exception e)
        {
            model.addAttribute("error", "XML processing failed: " + e.getMessage());
        }
        return "admin/xml_result";
    }

    // HIGH SEVERITY: Unvalidated Redirect
    @GetMapping("/redirect")
    public void redirectUser(@RequestParam("url") String url, HttpServletResponse response)
        throws IOException
    {
        // Open redirect vulnerability - no validation of redirect URL
        response.sendRedirect(url);
    }

    // HIGH SEVERITY: Information disclosure through error messages
    @GetMapping("/admin/debug")
    public String debugInfo(@RequestParam("userId") String userId, Model model)
    {
        try
        {
            // Exposing sensitive information in error messages
            String sql = "SELECT * FROM users WHERE id = " + userId;
            var user = entityManager.createNativeQuery(sql).getSingleResult();
            model.addAttribute("user", user);
        }
        catch (Exception e)
        {
            // Exposing full stack trace and SQL query to user
            model.addAttribute("error", "SQL Query: " + "SELECT * FROM users WHERE id = " + userId);
            model.addAttribute("stackTrace", e.toString());
            model.addAttribute("fullException", e);
        }
        return "admin/debug";
    }
}
