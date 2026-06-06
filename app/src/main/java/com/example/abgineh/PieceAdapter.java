package com.example.abgineh;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abgineh.myapp.R;

import java.util.List;

public class PieceAdapter
        extends RecyclerView.Adapter<PieceAdapter.Holder> {

    List<GlassPiece> list;

    Runnable onChange;

    public PieceAdapter(
            List<GlassPiece> list,
            Runnable onChange

    ) {

        this.list = list;

        this.onChange = onChange;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_piece,
                        parent,
                        false

                );

        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(
            @NonNull Holder holder,
            @SuppressLint("RecyclerView") int position
    ) {

        GlassPiece p =
                list.get(position);

        holder.txtPiece.setText(

                p.width +
                        " × " +
                        p.height +
                        "   تعداد: " +
                        p.quantity

        );

        holder.btnDelete.setOnClickListener(
                new View.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(View v) {

                        list.remove(position);

                        notifyDataSetChanged();

                        onChange.run();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class Holder
            extends RecyclerView.ViewHolder {

        TextView txtPiece;

        Button btnDelete;

        public Holder(@NonNull View itemView) {
            super(itemView);

            txtPiece =
                    itemView.findViewById(
                            R.id.txtPiece
                    );

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete
                    );
        }
    }
}