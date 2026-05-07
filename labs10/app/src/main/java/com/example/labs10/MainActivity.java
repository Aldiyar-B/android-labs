package com.example.labs10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_PLACE_ID = "place_id";
    public static final String PREFS_NAME = "quiz_progress";

    private static final Place[] PLACES = {
            new Place("moscow", "Москва", 55.753688, 37.622037),
            new Place("paris", "Париж", 48.856651, 2.351691),
            new Place("rio", "Рио-де-Жанейро", -22.801122, -43.336894),
            new Place("sydney", "Сидней", -33.868820, 151.209290)
    };

    private final Map<String, PlacemarkMapObject> placemarks = new LinkedHashMap<>();
    private final MapObjectTapListener markerTapListener = this::openQuiz;

    private MapView mapView;
    private TextView progressText;
    private SharedPreferences progressPrefs;
    private ImageProvider redMarkerIcon;
    private ImageProvider greenMarkerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map_view);
        progressText = findViewById(R.id.progress_text);
        progressPrefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        redMarkerIcon = markerImageProvider(R.drawable.ic_marker_red);
        greenMarkerIcon = markerImageProvider(R.drawable.ic_marker_green);

        addMarkers();
        moveCameraToStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
        refreshProgress();
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    private void addMarkers() {
        com.yandex.mapkit.map.Map yandexMap = mapView.getMapWindow().getMap();
        for (Place place : PLACES) {
            PlacemarkMapObject placemark = yandexMap.getMapObjects().addPlacemark();
            placemark.setGeometry(new Point(place.latitude, place.longitude));
            placemark.setIcon(markerIcon(place.id));
            placemark.setUserData(place.id);
            placemark.setText(place.title);
            placemark.addTapListener(markerTapListener);
            placemarks.put(place.id, placemark);
        }
    }

    private boolean openQuiz(MapObject mapObject, Point point) {
        Object userData = mapObject.getUserData();
        if (userData instanceof String) {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra(EXTRA_PLACE_ID, (String) userData);
            startActivity(intent);
        }
        return true;
    }

    private void moveCameraToStart() {
        mapView.getMapWindow().getMap().move(
                new CameraPosition(new Point(20.0, 45.0), 1.45f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0.8f),
                null
        );
    }

    private void refreshProgress() {
        int completed = 0;
        for (Place place : PLACES) {
            if (isAnswered(place.id)) {
                completed++;
            }
            PlacemarkMapObject placemark = placemarks.get(place.id);
            if (placemark != null) {
                placemark.setIcon(markerIcon(place.id));
            }
        }
        progressText.setText("Собрано точек: " + completed + " из " + PLACES.length);
    }

    private ImageProvider markerIcon(String placeId) {
        return isAnswered(placeId) ? greenMarkerIcon : redMarkerIcon;
    }

    private ImageProvider markerImageProvider(int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        if (drawable == null) {
            throw new IllegalStateException("Marker drawable not found: " + drawableId);
        }

        int width = Math.max(drawable.getIntrinsicWidth(), 1);
        int height = Math.max(drawable.getIntrinsicHeight(), 1);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return ImageProvider.fromBitmap(bitmap);
    }

    private boolean isAnswered(String placeId) {
        return progressPrefs.getBoolean(placeId, false);
    }

    private static class Place {
        final String id;
        final String title;
        final double latitude;
        final double longitude;

        Place(String id, String title, double latitude, double longitude) {
            this.id = id;
            this.title = title;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}
