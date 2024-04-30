package com.example.WeatherCheck.mauritus;
import android.content.Context;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.WeatherCheck.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapViewHolder> {

    private Context context;

    public MapAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map, parent, false);
        return new MapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MapViewHolder holder, int position) {
        holder.initializeMapView();
    }

    @Override
    public int getItemCount() {
        return 1; }

    static class MapViewHolder extends RecyclerView.ViewHolder {
        MapView mapView;

        MapViewHolder(@NonNull View itemView) {
            super(itemView);
            mapView = itemView.findViewById(R.id.map);
        }

        void initializeMapView() {
            Configuration.getInstance().load(itemView.getContext(), androidx.preference.PreferenceManager.getDefaultSharedPreferences(itemView.getContext()));
            mapView.setTileSource(TileSourceFactory.MAPNIK);
            mapView.setBuiltInZoomControls(true);
            mapView.setMultiTouchControls(true);
            MapController mapController = (MapController) mapView.getController();
            mapController.setZoom(15.0);
            mapController.setCenter(new GeoPoint(-20.3484, 57.5522));

            // Add a red marker on the city with the city name
            ArrayList<OverlayItem> items = new ArrayList<>();
            GeoPoint cityLocation =  new GeoPoint(-20.3484, 57.5522); // City location
            OverlayItem cityOverlayItem = new OverlayItem("City", "Port Louis", cityLocation);
            Drawable cityMarker = createMarker(); // Create red marker
            cityOverlayItem.setMarker(cityMarker);
            items.add(cityOverlayItem);
            ItemizedIconOverlay<OverlayItem> itemizedIconOverlay = new ItemizedIconOverlay<>(items, cityMarker, null, mapView.getContext());
            mapView.getOverlays().add(itemizedIconOverlay);
        }

        private Drawable createMarker() {
            // Create a custom arrow drawable
            ShapeDrawable shapeDrawable = new ShapeDrawable(new PathShape(createArrowPath(), 30, 30));
            shapeDrawable.getPaint().setColor(0xffff0000); // Red color
            shapeDrawable.setIntrinsicHeight(60);
            shapeDrawable.setIntrinsicWidth(40);
            return shapeDrawable;
        }

        private Path createArrowPath() {
            Path path = new Path();
            path.moveTo(0, 0);
            path.lineTo(15, 30);
            path.lineTo(30, 0);
            path.close();
            return path;
        }

    }
}
