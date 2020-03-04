package com.patelheggere.krishna;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.data.Feature;
import com.google.maps.android.data.Layer;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPolygon;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class KmlDemoActivity extends BaseDemoActivity {

    private static final String TAG = "KmlDemoActivity";
    private GoogleMap mMap;
    private Button details;
    String ms;
    AlertDialog alertDialog;

    protected int getLayoutId() {
        return R.layout.kml_demo;
    }

    public void startDemo () {
        try {
            details = findViewById(R.id.details);
            mMap = getMap();
            final int val = getIntent().getIntExtra("MAP",0);
            //retrieveFileFromResource(val);
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(val==1)
                    {
                        ms = "This is Davanagere";
                    }
                    if(val==2)
                    {
                        ms = "This is Bellary";
                    }
                    if(val==3)
                    {
                        ms = "This is Bidar";
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(KmlDemoActivity.this);
                    builder.setMessage(ms);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            });
            retrieveFileFromResource(val);
           // retrieveFileFromUrl();
        } catch (Exception e) {
            Log.e("Exception caught", e.toString());
        }
    }

    private void retrieveFileFromResource(int val) {
        try {
            if(val==1) {
                KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.karnataka_red, getApplicationContext());
                kmlLayer.addLayerToMap();
                moveCameraToKml(kmlLayer);
                kmlLayer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
                    @Override
                    public void onFeatureClick(Feature feature) {
                        Log.d(TAG, "onFeatureClick: ");
                    }
                });
            }
            else if(val==2)
            {
                KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.karnataka_blue, getApplicationContext());
                kmlLayer.addLayerToMap();
                moveCameraToKml(kmlLayer);
                kmlLayer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
                    @Override
                    public void onFeatureClick(Feature feature) {
                        Log.d(TAG, "onFeatureClick: ");
                    }
                });
            }
            else if(val==3)
            {
                KmlLayer kmlLayer = new KmlLayer(mMap, R.raw.karnataka_green, getApplicationContext());
                kmlLayer.addLayerToMap();
                moveCameraToKml(kmlLayer);
                kmlLayer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
                    @Override
                    public void onFeatureClick(Feature feature) {
                        Log.d(TAG, "onFeatureClick: ");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void retrieveFileFromUrl() {
        new DownloadKmlFile("http://kusavinibalaga.org/hamsa/karnataka_blue.kml").execute();
    }

    private void moveCameraToKml(KmlLayer kmlLayer) {
        //Retrieve the first container in the KML layer
        KmlContainer container = kmlLayer.getContainers().iterator().next();
        //Retrieve a nested container within the first container
        container = container.getContainers().iterator().next();
        //Retrieve the first placemark in the nested container
        KmlPlacemark placemark = container.getPlacemarks().iterator().next();
        //Retrieve a polygon object in a placemark
        KmlPolygon polygon = (KmlPolygon) placemark.getGeometry();
        //Create LatLngBounds of the outer coordinates of the polygon
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : polygon.getOuterBoundaryCoordinates()) {
            builder.include(latLng);
        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        getMap().moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), width, height, 1));
    }

    private class DownloadKmlFile extends AsyncTask<String, Void, byte[]> {
        private final String mUrl;

        public DownloadKmlFile(String url) {
            mUrl = url;
        }

        protected byte[] doInBackground(String... params) {
            try {
                InputStream is =  new URL(mUrl).openStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(byte[] byteArr) {
            try {
                KmlLayer kmlLayer = new KmlLayer(mMap, new ByteArrayInputStream(byteArr),
                        getApplicationContext());
                kmlLayer.addLayerToMap();
                kmlLayer.setOnFeatureClickListener(new KmlLayer.OnFeatureClickListener() {
                    @Override
                    public void onFeatureClick(Feature feature) {
                        Toast.makeText(KmlDemoActivity.this,
                                "Feature clicked: " + feature.getId(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
                moveCameraToKml(kmlLayer);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}
