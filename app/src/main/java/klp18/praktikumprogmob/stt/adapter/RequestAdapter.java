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

import javax.annotation.Nonnull;

import klp18.praktikumprogmob.stt.DetailRequestAcaraPenggunaActivity;
import klp18.praktikumprogmob.stt.R;
import klp18.praktikumprogmob.stt.network.Request;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.HolderData> {

    private List<Request> list;
    private Context ctx;

    public RequestAdapter(List<Request> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_request, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@Nonnull HolderData holder, int position) {
        Request request = list.get(position);
        holder.idAcara.setText(request.getId());
        holder.nama.setText(request.getNama_acara_request());
        holder.tanggal.setText(request.getTanggal());
        holder.lokasi.setText(request.getLokasi());
        holder.keterangan.setText(request.getKeterangan());
        holder.namaUser.setText(request.getNama_pengguna());
        holder.alamat.setText(request.getAlamat());
        holder.request = request;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HolderData extends RecyclerView.ViewHolder {
        TextView idAcara, nama, tanggal, lokasi, keterangan, namaUser, alamat;
        Request request;
        public HolderData(@NonNull View v) {
            super(v);
            idAcara = v.findViewById(R.id.idRequestAcara);
            nama = v.findViewById(R.id.tvNamaAcaraRequestPengguna);
            tanggal = v.findViewById(R.id.tvTanggalAcaraRequestPengguna);
            lokasi = v.findViewById(R.id.tvLokasiAcaraRequestPengguna);
            keterangan = v.findViewById(R.id.tvKeteranganRequestPengguna);
            namaUser = v.findViewById(R.id.tvNamaPenggunaRequest);
            alamat = v.findViewById(R.id.tvAlamatRequestPengguna);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent editRequest = new Intent(ctx, DetailRequestAcaraPenggunaActivity.class);
                    editRequest.putExtra("id", request.getId());
                    editRequest.putExtra("nama", request.getNama_acara_request());
                    editRequest.putExtra("tanggal", request.getTanggal());
                    editRequest.putExtra("lokasi", request.getLokasi());
                    editRequest.putExtra("keterangan", request.getKeterangan());
                    editRequest.putExtra("namaUser", request.getNama_pengguna());
                    editRequest.putExtra("alamat", request.getAlamat());
                    ctx.startActivity(editRequest);
                }
            });
        }
    }
}
