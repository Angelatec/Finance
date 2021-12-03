package mx.tec.finance


import android.content.ContentValues
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class Adapter(private var figure: ArrayList<ArrayList<String>>, private var listener: View.OnClickListener)  : RecyclerView.Adapter<Adapter.FigureViewHolder>() {


    class FigureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        lateinit var category : TextView
        lateinit var amount : TextView

        init {
            category = itemView.findViewById(R.id.categoria)
            amount=itemView.findViewById(R.id.cantidad)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FigureViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.movements_row, parent, false)
        val dvh = FigureViewHolder(view)

        view.setOnClickListener(listener)
        return dvh
    }


    override fun onBindViewHolder(holder: FigureViewHolder, position: Int) {
        holder.category.text = figure[position][3]
        holder.amount.text = figure[position][4]

        if(position%2 == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFDBDDDD"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFEFEDED"));

        }
    }

    override fun getItemCount(): Int {
        return figure.size
    }

}