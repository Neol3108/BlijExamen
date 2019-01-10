package in.hageste.noel.blijexamen.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import in.hageste.noel.blijexamen.R;
import in.hageste.noel.blijexamen.models.Feeding;
import in.hageste.noel.blijexamen.models.Route;

public class FeedingsListAdapter extends RecyclerView.Adapter<FeedingsListAdapter.CustomViewHolder> {
    private List<Feeding> feedings;
    private Context context;

    public FeedingsListAdapter(Context context, List<Feeding> feedings) {
        this.feedings = feedings;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feeding_list_item, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewHolder, int i) {
        Feeding feeding = feedings.get(i);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.US);

        viewHolder.name.setText(feeding.getAnimal().getName());
        viewHolder.time.setText(String.format(context.getResources().getString(R.string.timeDisplay), sdf.format(feeding.getTime().getTime()), sdf.format(feeding.getEndTime().getTime())));
    }

    @Override
    public int getItemCount() {
        return feedings.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView time;

        public CustomViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
        }
    }
}
