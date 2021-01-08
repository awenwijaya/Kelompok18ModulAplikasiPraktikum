package klp18.praktikumprogmob.stt.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import klp18.praktikumprogmob.stt.R;
import klp18.praktikumprogmob.stt.database.Event;
import klp18.praktikumprogmob.stt.database.SQLiteHelper;
import klp18.praktikumprogmob.stt.events.DetailEventActivity;
import klp18.praktikumprogmob.stt.events.EditEventActivity;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.HolderData> {

    private List<Event> list;
    private Context ctx;
    SQLiteHelper helper;

    public ListAdapter(List<Event> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_event, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Event event = list.get(position);
        holder.tId.setText(event.getId());
        holder.tNama.setText(event.getNama_acara());
        holder.tTanggal.setText(event.getTanggal_acara());
        holder.tTempat.setText(event.getTempat_acara());
        holder.tAlamat.setText(event.getAlamat());
        holder.tKeterangan.setText(event.getKeterangan());
        holder.event = event;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderData extends RecyclerView.ViewHolder{
        TextView tId, tNama, tTanggal, tTempat, tAlamat, tKeterangan;
        Event event;
        public HolderData(View v) {
            super(v);
            tId = v.findViewById(R.id.idAcara);
            tNama = v.findViewById(R.id.tvNama);
            tTanggal = v.findViewById(R.id.tvTanggalAcara);
            tTempat = v.findViewById(R.id.tvTempatAcara);
            tAlamat = v.findViewById(R.id.tvAlamat);
            tKeterangan = v.findViewById(R.id.tvKeterangan);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gotoEdit = new Intent(ctx, DetailEventActivity.class);
                    gotoEdit.putExtra("id", event.getId());
                    gotoEdit.putExtra("nama_acara", event.getNama_acara());
                    gotoEdit.putExtra("tanggal_acara", event.getTanggal_acara());
                    gotoEdit.putExtra("tempat_acara", event.getTempat_acara());
                    gotoEdit.putExtra("alamat", event.getAlamat());
                    gotoEdit.putExtra("keterangan", event.getKeterangan());
                    ctx.startActivity(gotoEdit);
                }
            });
        }
    }
}
