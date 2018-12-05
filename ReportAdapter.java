package dk.ikas.lcd.examproject;

//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.List;
//
//public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        private CardView cardView;
//
//        public ViewHolder(CardView v) {
//            super(v);
//            this.cardView = v;
//        }
//    }
//
//    private List<Report> reports;
//    private Context context;
//
//    public ReportAdapter(Context context) {
//        this.context = context;
//    }
//
//    public void setList(List<Report> report) {
//        this.reports = report;
//    }
//
//    @NonNull
//    @Override
//    public ReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//
//        //CardView cardView = (CardView) LayoutInflater.from(this.context).inflate(R.id.report_list_card_view, viewGroup, false);
//        CardView cardView = (CardView) viewGroup.findViewById(R.id.card_view);
//        return new ViewHolder(cardView);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ReportAdapter.ViewHolder viewHolder, int i) {
//
//        CardView cardView = viewHolder.cardView;
//        ImageView imageView = (ImageView) cardView.findViewById(R.id.picture);
//        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), i);
//        imageView.setImageDrawable(drawable);
//        imageView.setContentDescription(reports.get(i).getDate());
//        TextView textView = (TextView) cardView.findViewById(R.id.uuid);
//        textView.setText(reports.get(i).getUuid());
//    }
//
//    @Override
//    public int getItemCount() {
//
//        return this.reports.size();
//
//    }
//}
