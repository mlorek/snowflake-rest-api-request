package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class RestApiRequest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        // register for a free account at www.snowflake.com
        String account = "";
        // generate using https://github.com/mlorek/snowflake-jwt-generator
        String jwt = "";
        String postData = "{ \"statement\" : \"select current_account(), current_timestamp()\" }";
        connect(account, jwt, postData);
    }

    public static String connect(String account, String jwt, String postData) throws IOException {
        URL myURL = new URL(String.format("https://%s.snowflakecomputing.com/api/v2/statements", account));
        HttpsURLConnection connection = (HttpsURLConnection) myURL.openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + jwt);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Length", "" + postData.getBytes().length);
        connection.setRequestProperty("User-Agent", "java/1.0");
        connection.setRequestProperty("X-Snowflake-Authorization-Token-Type", "KEYPAIR_JWT");

        connection.setRequestMethod("POST");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(postData.getBytes());

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);
        String responseMsg = connection.getResponseMessage();
        System.out.println(responseMsg);

        try {
            switch (responseCode) {
                case 200: {
                    InputStream is = connection.getInputStream();
                    SnowflakeResponse response = processInputStream(is, SnowflakeResponse.class);
                    System.out.println(response.sqlState);
                    break;
                }
                // Unprocessable Entity
                case 422: {
                    InputStream es = connection.getErrorStream();
                    ErrorResponse response = processInputStream(es, ErrorResponse.class);
                    System.out.println(response.sqlState);
                    break;
                }
                default: {
                    throw new RuntimeException("unhandled: " + responseCode);
                }
            }
        } finally {
            connection.disconnect();
        }

        return "";
    }

    private static <T> T processInputStream(InputStream is, Class<T> tClass) throws IOException {
        byte[] bytes = is.readAllBytes();
        String res = new String(bytes);
        System.out.println(res);
        return MAPPER.readValue(bytes, tClass);
    }
}
