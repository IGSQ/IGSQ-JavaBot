package org.igsq.igsqbot.util;

import org.igsq.igsqbot.Yaml;

import java.io.*;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class APIUtils
{
	public static final String USER_AGENT = "IGSQBOT/1.0";

	public static String getAUTH(String auth)
	{
		return Base64.getEncoder().encodeToString((auth).getBytes());
	}
	public static String sendGET(String url)
	{
		try
		{
			// Get and open a connection, stored in connection object
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// Set ourselves as if we're "vaguely netscape"
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);

			// Handle String
			return (handleHTTPResponse(con));
		}
		catch(Exception exception)
		{
			return "";
		}
	}

	public static String sendPOST(String url) { return sendPOST(url, ""); }
	public static String sendPOST(String url, String params)
	{
		try
		{
			// Get and open a connection, stored in connection object
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// Set ourselves as if we're "vaguely netscape"
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);

			// For POST only - Configure parameters
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(params.getBytes());
			os.flush();
			os.close();

			// Handle String
			return (handleHTTPResponse(con));
		}
		catch(Exception exception)
		{
			return "";
		}
	}

	public static String sendPOST(String url, String params, String auth)
	{
		try
		{
			// Get and open a connection, stored in connection object
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// Set ourselves as if we're "vaguely netscape"
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Authorization", "Basic " + auth);

			// For POST only - Configure parameters
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(params.getBytes());
			os.flush();
			os.close();

			// Handle String
			return (handleHTTPResponse(con));
		}
		catch(Exception exception)
		{
			return "";
		}
	}

	public static String handleHTTPResponse(HttpURLConnection con) throws IOException
	{
		// Check String code and act on it accordingly.
		int responseCode = con.getResponseCode();

		// If we succeeded...
		if (responseCode == HttpURLConnection.HTTP_OK)
		{
			// Variable declaration
			String inputLine;
			StringBuilder resp = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

			// Read all lines from the String
			inputLine = in.readLine();
			while (inputLine != null)
			{
				resp.append('\n');
				resp.append(inputLine);
				inputLine = in.readLine();
			}

			// Close and return our data
			in.close();
			return resp.toString();
		}
		else
		{
			return "" + responseCode;
		}
	}

	public static String getAPIToken(String service)
	{
		return YamlUtils.isFieldEmpty("bot.api" + service, "config") ? null : Yaml.getFieldString("bot.api." + service, "config");
	}
}
