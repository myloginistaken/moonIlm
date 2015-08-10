package anton.ilm.asyncTask;

import android.os.AsyncTask;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import anton.ilm.interfaces.OnTaskCompleted;
import anton.ilm.objects.Forecast;
import anton.ilm.parsers.XmlParser;

/**
 * Created by anton on 22.05.15.
 */
public class DownloadXMLTask extends AsyncTask<String, Void, List<Forecast>> {

    private OnTaskCompleted listener;

    public DownloadXMLTask(OnTaskCompleted listener){
        this.listener=listener;
    }

    @Override
    protected List doInBackground(String... urls) {
        //TODO:Send correct error message in a smart way
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            //list.add(ctx.getResources().getString(R.string.connection_error));
            return null;
        } catch (XmlPullParserException e) {
            //list.add(ctx.getResources().getString(R.string.xml_error));
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Forecast> result) {
        //TODO: avoid hardcoded indexes
        if(result!=null) {
            listener.onTaskCompleted(result);
        } else {
            //date.setText(ctx.getResources().getString(R.string.connection_or_xml_error));
            Log.e("XML", "Connection or XML error");
        }
    }

    private List loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        // Instantiate the parser
        XmlParser xmlParser = new XmlParser();
        List<Forecast> forecasts = null;
        try {
            stream = downloadUrl(urlString);
            forecasts = xmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return forecasts;
    }

    // Given a string representation of a URL, sets up a connection and gets
// an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
