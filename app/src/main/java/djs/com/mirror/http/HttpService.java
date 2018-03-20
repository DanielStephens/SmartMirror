package djs.com.mirror.http;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import djs.com.mirror.ui.MainActivity;

/**
 * Created by Daniel on 24/02/2018.
 */

public class HttpService {

    private final DocumentBuilder documentBuilder;

    public HttpService() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
//    Document document = db.parse(source);

    public String asString(String address) throws IOException {
        return retrieveAndConvert(address, new IOFunction<BufferedReader, String>() {
            @Override
            public String apply(BufferedReader bufferedReader) throws IOException {
                StringBuilder responseStrBuilder = new StringBuilder(2048);

                String inputStr;
                while ((inputStr = bufferedReader.readLine()) != null) {
                    responseStrBuilder.append(inputStr);
                }
                return responseStrBuilder.toString();
            }
        });
    }

    public Document asXml(String address) throws IOException {
        return retrieveAndConvert(address, new IOFunction<BufferedReader, Document>() {
            @Override
            public Document apply(BufferedReader bufferedReader) throws IOException {
                InputSource source = new InputSource(bufferedReader);
                try {
                    return documentBuilder.parse(source);
                } catch (SAXException e) {
                    e.printStackTrace();
                    throw new IOException(e);
                }
            }
        });
    }

    public JSONObject asJson(String address) throws IOException, JSONException {
        return new JSONObject(asString(address));
    }

    private <T> T retrieveAndConvert(String address, IOFunction<BufferedReader, T> conversion) throws IOException {
        MainActivity.print("getting " + address);
        URL url = new URL(address);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            return conversion.apply(new BufferedReader(new InputStreamReader(in, "UTF-8")));
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        } finally {
            urlConnection.disconnect();
        }
    }

    private interface IOFunction<A, B> {

        B apply(A a) throws IOException;

    }


}


