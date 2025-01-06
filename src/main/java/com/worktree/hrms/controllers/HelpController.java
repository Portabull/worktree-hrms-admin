package com.worktree.hrms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.worktree.hrms.utils.DateUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api")
public class HelpController {

//    [
//    {
//        "id": "internet",
//            "title": "Check Internet Connection",
//            "description": "Ensure you have a stable internet connection. Test by opening other websites to confirm connectivity.",
//            "steps": [
//        "Restart your router or modem.",
//                "Check your device's network settings.",
//                "Contact your Internet Service Provider if the issue persists."
//        ]
//    },
//    {
//        "id": "credentials",
//            "title": "Verify Credentials",
//            "description": "Ensure your username and password are correct. If you’ve forgotten your password, click 'Forgot password?' to reset it.",
//            "steps": [
//        "Double-check your username or email address.",
//                "Reset your password using the 'Forgot password?' link.",
//                "Contact support if your credentials are locked."
//        ]
//    },
//    {
//        "id": "location",
//            "title": "Enable Location Services",
//            "description": "Location services may be required to verify your login. Follow these steps to enable location:",
//            "steps": [
//        "On Chrome (Windows/Mac): Go to Settings > Privacy and Security > Site Settings > Location. Enable 'Ask before accessing'.",
//                "On Firefox (Windows/Mac): Go to Preferences > Privacy & Security > Permissions > Location. Enable location access for your site.",
//                "On Safari (Mac/iPhone): Go to Preferences > Websites > Location. Select 'Allow' for your website.",
//                "On Android: Open Settings > Location. Ensure location services are turned on.",
//                "On iOS: Go to Settings > Privacy > Location Services. Enable location services and set your browser to 'While Using the App'."
//        ]
//    },
//    {
//        "id": "cache",
//            "title": "Clear Browser Cache",
//            "description": "Clearing the cache can resolve login issues. Check your browser settings to clear cache and cookies.",
//            "steps": [
//        "On Chrome: Go to Settings > Privacy and Security > Clear Browsing Data.",
//                "On Firefox: Go to Options > Privacy & Security > Cookies and Site Data > Clear Data.",
//                "On Safari: Go to Preferences > Privacy > Manage Website Data > Remove All."
//        ]
//    },
//    {
//        "id": "compatibility",
//            "title": "Browser Compatibility",
//            "description": "Ensure you are using a supported browser (e.g., Chrome, Firefox, Edge). Update your browser to the latest version if needed.",
//            "steps": [
//        "Use a modern browser like Chrome, Firefox, or Edge.",
//                "Update your browser to the latest version.",
//                "Disable outdated plugins or extensions that may interfere."
//        ]
//    },
//    {
//        "id": "extensions",
//            "title": "Disable Extensions",
//            "description": "Browser extensions may interfere with the login process. Disable them temporarily and try again.",
//            "steps": [
//        "Open your browser's extensions or add-ons page.",
//                "Disable any extensions related to ad-blocking or script blocking.",
//                "Restart your browser and try logging in again."
//        ]
//    },
//    {
//        "id": "account",
//            "title": "Ensure Your Account is Active",
//            "description": "If your account is deactivated, contact your administrator to reactivate it.",
//            "steps": [
//        "Verify your account status in your profile settings.",
//                "Contact your administrator for reactivation if needed."
//        ]
//    },
//    {
//        "id": "support",
//            "title": "Contact Support",
//            "description": "If the above steps don’t resolve the issue, contact our support team.",
//            "steps": [
//        "Email our support team at support@example.com.",
//                "Provide details about the issue you are facing.",
//                "Wait for a response within 24-48 hours."
//        ]
//    }
//]


    String conf = "[\n" +
            "    {\n" +
            "        \"id\": \"email\",\n" +
            "        \"title\": \"Email Configuration\",\n" +
            "        \"description\": \"Configure the email properties required for sending emails from the system. This includes setting up the email host, port, credentials, and additional properties to ensure proper email functionality.\",\n" +
            "        \"steps\": [\n" +
            "            \"Navigate to the Email section by clicking the 'Email' icon on the settings dashboard.\",\n" +
            "            \"Fill in the required fields:\\n- **Email Host**: The SMTP server address (e.g., email-smtp.us-east-2.amazonaws.com).\\n- **Email Port**: The port number (e.g., 587 for TLS or 465 for SSL).\\n- **Username**: The username for your email provider.\\n- **Password**: The password associated with the email username.\",\n" +
            "            \"Enter the 'Email From' address that will appear as the sender of the emails.\",\n" +
            "            \"Specify additional properties to fine-tune the SMTP settings. Common properties include:\\n  - mail.transport.protocol: Set to 'smtp'.\\n  - mail.smtp.auth: Set to 'true' to enable authentication.\\n  - mail.smtp.starttls.enable: Set to 'true' to enable TLS.\\n  - mail.debug: Set to 'true' for debugging purposes.\",\n" +
            "            \"Optionally, use the 'Export' button to save the configuration or 'Import' button to upload an existing configuration file.\",\n" +
            "            \"Click the 'Submit' button to save the configuration.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"mobile\",\n" +
            "        \"title\": \"Mobile Configuration\",\n" +
            "        \"description\": \"Set up mobile messaging providers like Twilio or TextLocal to enable SMS or WhatsApp communication.\",\n" +
            "        \"steps\": [\n" +
            "            \"Navigate to the Mobile section by clicking the 'Mobile' icon on the settings dashboard.\",\n" +
            "            \"Select the mobile provider (e.g., Twilio, TextLocal) from the dropdown menu.\",\n" +
            "            \"Fill in the required fields:\\n- **Account SID**: Enter your Account SID provided by the mobile provider.\\n- **Auth Token**: Enter the authentication token.\\n- **Mobile Number**: Provide the primary mobile number.\\n- **WhatsApp Mobile Number**: Specify the WhatsApp-enabled mobile number.\",\n" +
            "            \"Optionally, add additional properties like custom parameters using the 'Additional Properties' section.\",\n" +
            "            \"Use the 'Export' button to save the configuration or the 'Import' button to upload an existing configuration.\",\n" +
            "            \"Click the 'Submit' button to save the configuration.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"ai\",\n" +
            "        \"title\": \"AI Configuration\",\n" +
            "        \"description\": \"Configure AI services like OpenAI or Azure AI to integrate machine learning capabilities.\",\n" +
            "        \"steps\": [\n" +
            "            \"Navigate to the AI section by clicking the 'AI' icon on the settings dashboard.\",\n" +
            "            \"Select the AI provider (e.g., OpenAI, Azure AI) from the dropdown menu.\",\n" +
            "            \"For OpenAI:\\n- **AI Key**: Enter your API key.\\n- **Model**: Specify the model name (e.g., gpt-4.0).\\n- **Version**: Provide the version if applicable.\",\n" +
            "            \"For Azure AI:\\n- **Endpoint**: Enter the Azure AI endpoint.\\n- **Model**: Specify the model name.\\n- **Version**: Provide the version.\\n- **Deployment Name**: Enter the deployment name for Azure AI.\",\n" +
            "            \"Use the 'Export' button to save the configuration or the 'Import' button to upload an existing configuration.\",\n" +
            "            \"Click the 'Submit' button to save the configuration.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"coupons\",\n" +
            "        \"title\": \"Coupons Management\",\n" +
            "        \"description\": \"Manage discount coupons by adding, editing, or filtering coupon codes.\",\n" +
            "        \"steps\": [\n" +
            "            \"Navigate to the Coupons section by clicking the 'Coupons' icon on the settings dashboard.\",\n" +
            "            \"To add a coupon:\\n- Click the 'Add Coupon' button.\\n- Provide details such as the coupon code, discount percentage, and status.\\n- Click 'Save' to add the coupon.\",\n" +
            "            \"To edit a coupon:\\n- Click on a coupon in the table.\\n- Modify details such as the discount percentage and status in the popup.\\n- Click 'Save' to update the coupon.\",\n" +
            "            \"Use the filter box to search for specific coupon codes.\",\n" +
            "            \"The table displays details such as Coupon Code, Discount Percentage, Created Date, and Created By.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"keystore\",\n" +
            "        \"title\": \"Keystore Configuration\",\n" +
            "        \"description\": \"Configure and manage keystore properties for secure communication and certificate linking.\",\n" +
            "        \"steps\": [\n" +
            "            \"Navigate to the Keystore section by clicking the 'Keystore' icon on the settings dashboard.\",\n" +
            "            \"Fill in the required fields:\\n- **Algorithm**: Select the algorithm (e.g., RSA).\\n- **Alias Name**: Enter a unique alias name.\\n- **Key Size**: Specify the key size (e.g., 2048).\\n- **Validity (Days)**: Enter the validity period in days.\\n- **Keystore Password**: Provide a secure password for the keystore.\\n- **Common Name (CN)**: Specify the common name.\\n- **Organization Unit (OU)**: Enter the organizational unit.\\n- **Organization (O)**: Provide the organization name.\\n- **Locality (L)**: Specify the locality.\\n- **State (S)**: Enter the state.\\n- **Country Code (C)**: Provide the country code.\",\n" +
            "            \"Optionally, add additional properties in the 'Additional Properties' section.\",\n" +
            "            \"Click the 'Generate' button to create the keystore.\",\n" +
            "            \"To link a certificate:\\n- Click 'Link Cert' in the Certificate Linking popup.\\n- Download the linked certificate if required.\"\n" +
            "        ]\n" +
            "    },\n" +

            "    {\n" +
            "        \"id\": \"proxy\",\n" +
            "        \"title\": \"Proxy Configuration\",\n" +
            "        \"description\": \"Configure and manage proxy settings to control and secure outbound API requests through a proxy server.\",\n" +
            "        \"steps\": [\n" +
            "            \"Navigate to the Proxy Configuration section by clicking the 'Proxy' icon on the settings dashboard.\",\n" +
            "            \"Enable or disable proxy usage by toggling the 'Enable Proxy' option. When enabled, all outbound API requests will be routed through the configured proxy server for enhanced security and monitoring.\",\n" +
            "            \"Fill in the required fields for HTTP Proxy configuration:\\n- **HTTP Host**: Specify the hostname or IP address of the HTTP proxy server.\\n- **HTTP Port**: Enter the port number of the HTTP proxy server.\\n- **HTTP Username**: Provide the username for authentication with the proxy server (if applicable).\\n- **HTTP Password**: Provide the password for authentication with the proxy server (if applicable).\",\n" +
            "            \"For HTTPS Proxy configuration, you can select the 'Use same as HTTP' option or provide separate details for HTTPS Host, Port, Username, and Password.\",\n" +
            "            \"Understand the benefits of using a proxy:\\n- **Security**: Adds an additional layer of protection by masking the server's IP address and filtering traffic.\\n- **Monitoring**: Allows administrators to monitor, log, and control outbound requests effectively.\\n- **Compliance**: Helps enforce company policies for internet usage and data access.\",\n" +
            "            \"Optionally, configure advanced settings, such as bypassing the proxy for specific URLs or IP ranges in the 'Advanced Configuration' section.\",\n" +
            "            \"Test the proxy configuration by saving the settings. The system will automatically validate the connection to the proxy server before completing the save. If any error occurs during the validation, an error message will be displayed, and the settings will not be saved until the issue is resolved.\",\n" +
            "            \"Ensure that the proxy server is reachable from the application server. If the proxy server is down or unreachable, all outbound API requests will fail. This setup provides administrators the ability to turn off or force-stop internet access on the server by disabling the proxy or making it unreachable.\",\n" +
            "            \"Click the 'Save' button to apply the proxy configuration. Changes will take effect immediately for all outbound API requests.\",\n" +
            "            \"To disable proxy usage, simply toggle off the 'Enable Proxy' option. This will revert to direct API communication without routing through the proxy server.\"\n" +
            "        ]\n" +
            "    },\n" +

            "    {\n" +
            "        \"id\": \"storage\",\n" +
            "        \"title\": \"Storage Configuration\",\n" +
            "        \"description\": \"Configure storage settings for AWS S3, Google Cloud Storage, Azure ADLS, or Server Local Storage.\",\n" +
            "        \"steps\": [\n" +
            "            \"Navigate to the Storage section by clicking the 'Storage' icon on the settings dashboard.\",\n" +
            "            \"Select the storage provider (e.g., AWS S3, Google Cloud Storage, Azure ADLS, or Server Local Storage) from the dropdown menu.\",\n" +
            "            \"For AWS S3:\\n- **AWS Access Key**: Enter the access key.\\n- **AWS Secret Key**: Enter the secret key.\\n- **AWS Bucket Name**: Provide the S3 bucket name.\\n- **AWS Region**: Specify the AWS region.\",\n" +
            "            \"For Google Cloud Storage:\\n- **Project ID**: Enter the project ID.\\n- **Bucket Name**: Provide the bucket name.\\n- **Credentials File**: Upload the credentials JSON file.\",\n" +
            "            \"For Azure ADLS:\\n- **Account Endpoint**: Provide the Azure Blob Storage endpoint.\\n- **Account Name**: Enter the account name.\\n- **Account Key/SAS Token**: Provide the key or token.\\n- **Container Name**: Specify the container name.\",\n" +
            "            \"For Server Local Storage:\\n- **File Location**: Provide the file location (e.g., c:/admin/files/).\",\n" +
            "            \"Optionally, add additional properties using the 'Additional Properties' section.\",\n" +
            "            \"Use the 'Export' button to save the configuration or the 'Import' button to upload an existing configuration.\",\n" +
            "            \"Click the 'Submit' button to save the configuration.\"\n" +
            "        ]\n" +
            "    }\n" +
            "]";



    String a = "[\n" +
            "    {\n" +
            "        \"id\": \"internet\",\n" +
            "        \"title\": \"Check Internet Connection\",\n" +
            "        \"description\": \"Ensure you have a stable internet connection. Test by opening other websites to confirm connectivity.\",\n" +
            "        \"steps\": [\n" +
            "            \"Restart your router or modem.\",\n" +
            "            \"Check your device's network settings.\",\n" +
            "            \"Contact your Internet Service Provider if the issue persists.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"credentials\",\n" +
            "        \"title\": \"Verify Credentials\",\n" +
            "        \"description\": \"Ensure your username and password are correct. If you’ve forgotten your password, click 'Forgot password?' to reset it.\",\n" +
            "        \"steps\": [\n" +
            "            \"Double-check your username or email address.\",\n" +
            "            \"Reset your password using the 'Forgot password?' link.\",\n" +
            "            \"Contact support if your credentials are locked.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"location\",\n" +
            "        \"title\": \"Enable Location Services\",\n" +
            "        \"description\": \"Location services may be required to verify your login. Follow these steps to enable location:\",\n" +
            "        \"steps\": [\n" +
            "            \"On Chrome (Windows/Mac): Go to Settings > Privacy and Security > Site Settings > Location. Enable 'Ask before accessing'.\",\n" +
            "            \"On Firefox (Windows/Mac): Go to Preferences > Privacy & Security > Permissions > Location. Enable location access for your site.\",\n" +
            "            \"On Safari (Mac/iPhone): Go to Preferences > Websites > Location. Select 'Allow' for your website.\",\n" +
            "            \"On Android: Open Settings > Location. Ensure location services are turned on.\",\n" +
            "            \"On iOS: Go to Settings > Privacy > Location Services. Enable location services and set your browser to 'While Using the App.'\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"cache\",\n" +
            "        \"title\": \"Clear Browser Cache\",\n" +
            "        \"description\": \"Clearing the cache can resolve login issues. Check your browser settings to clear cache and cookies.\",\n" +
            "        \"steps\": [\n" +
            "            \"On Chrome: Go to Settings > Privacy and Security > Clear Browsing Data.\",\n" +
            "            \"On Firefox: Go to Options > Privacy & Security > Cookies and Site Data > Clear Data.\",\n" +
            "            \"On Safari: Go to Preferences > Privacy > Manage Website Data > Remove All.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"compatibility\",\n" +
            "        \"title\": \"Browser Compatibility\",\n" +
            "        \"description\": \"Ensure you are using a supported browser (e.g., Chrome, Firefox, Edge). Update your browser to the latest version if needed.\",\n" +
            "        \"steps\": [\n" +
            "            \"Use a modern browser like Chrome, Firefox, or Edge.\",\n" +
            "            \"Update your browser to the latest version.\",\n" +
            "            \"Disable outdated plugins or extensions that may interfere.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"extensions\",\n" +
            "        \"title\": \"Disable Extensions\",\n" +
            "        \"description\": \"Browser extensions may interfere with the login process. Disable them temporarily and try again.\",\n" +
            "        \"steps\": [\n" +
            "            \"Open your browser's extensions or add-ons page.\",\n" +
            "            \"Disable any extensions related to ad-blocking or script blocking.\",\n" +
            "            \"Restart your browser and try logging in again.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"account\",\n" +
            "        \"title\": \"Ensure Your Account is Active\",\n" +
            "        \"description\": \"If your account is deactivated, contact your administrator to reactivate it.\",\n" +
            "        \"steps\": [\n" +
            "            \"Verify your account status in your profile settings.\",\n" +
            "            \"Contact your administrator for reactivation if needed.\"\n" +
            "        ]\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"support\",\n" +
            "        \"title\": \"Contact Support\",\n" +
            "        \"description\": \"If the above steps don’t resolve the issue, contact our support team.\",\n" +
            "        \"steps\": [\n" +
            "            \"Email our support team at support@example.com.\",\n" +
            "            \"Provide details about the issue you are facing.\",\n" +
            "            \"Wait for a response within 24-48 hours.\"\n" +
            "        ]\n" +
            "    }\n" +
            "]";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DateUtils dateUtils;

    long lastModified;

    @PostConstruct
    public void loadDate() {
        lastModified = dateUtils.getCurrentDate().getTime();
    }


    @GetMapping("help-docs")
    public ResponseEntity<Map<String, Object>> getHelpDocs(@RequestParam String type) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        response.put("sideBarHeader", "Login Troubleshoot");
        response.put("mainBarHeader", "Troubleshooting Login Issues");
        response.put("mainArea", objectMapper.readValue(a, List.class));


        if(type.equalsIgnoreCase("conf")){
            response.put("mainArea", objectMapper.readValue(conf, List.class));
            response.put("sideBarHeader", "Storage Configuration");
            response.put("mainBarHeader", "Storage Configuration Guide");
        }

        // Generate ETag
//        String etag = Integer.toHexString(response.hashCode());
        HttpHeaders headers = new HttpHeaders();
//        headers.setETag("\"" + etag + "\"");
//
//        // Set Cache-Control and Last-Modified
        headers.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES).cachePublic());
//
//        headers.setLastModified(lastModified);
//
//        // Check If-None-Match
//        String ifNoneMatch = request.getHeader("If-None-Match");
//        if (ifNoneMatch != null && ifNoneMatch.equals("\"" + etag + "\"")) {
//            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).headers(headers).build();
//        }

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

}
