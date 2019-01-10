package in.hageste.noel.blijexamen.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.hageste.noel.blijexamen.R;
import in.hageste.noel.blijexamen.models.Route;

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.CustomViewHolder> {
    private List<Route> routes;
    private Context context;
    private OnItemClickListener listener;

    public RouteListAdapter(Context context, List<Route> routes, OnItemClickListener listener) {
        this.routes = routes;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.route_list_item, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, int i) {
        Route route = routes.get(i);

        viewHolder.name.setText(route.getName());
        viewHolder.animalList.setText(route.getFeedingsList());

        viewHolder.bind(route, listener);
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView animalList;
        protected TextView time;

        public CustomViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            animalList = view.findViewById(R.id.animalList);
            time = view.findViewById(R.id.time);
        }

        public void bind(final Route item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Route route);
    }
}
