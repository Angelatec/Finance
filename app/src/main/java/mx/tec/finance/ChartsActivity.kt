package mx.tec.finance

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries

class ChartsActivity : AppCompatActivity() {
    lateinit var email: String
    var expenses: ArrayList<Long> =ArrayList()
    var entries:  ArrayList<Long> =ArrayList()
    lateinit var  graphView : GraphView
    lateinit var  graphView2 : GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

        graphView = findViewById(R.id.idGraphView)
        graphView2 = findViewById(R.id.idGraphView2)

        email= intent.getStringExtra("email").toString()

        lateinit var series: LineGraphSeries<DataPoint>
        series = LineGraphSeries<DataPoint>();
        lateinit var series2: LineGraphSeries<DataPoint>
        series2 = LineGraphSeries<DataPoint>();

        var aux= Firebase.firestore.collection("movimientos").document(email).collection("gastos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    expenses.add((document.data?.values?.elementAt(3) as Long)*(-1))
                }
                for(i in 0 until expenses.size){
                    Log.w(i.toString(),expenses[i].toDouble().toString())

                    series.appendData(DataPoint(i.toDouble() ,expenses[i].toDouble()), true, 30)
                }
                graphView.addSeries(series)
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
        var aux1=Firebase.firestore.collection("movimientos").document(email).collection("ingresos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    entries.add(document.data?.values?.elementAt(3) as Long)
                }
                for(i in 0 until entries.size){
                    Log.w(i.toDouble().toString(),entries[i].toDouble().toString())
                    series2.appendData(DataPoint(i.toDouble(),entries[i].toDouble()), true, 30)
                }
                graphView2.addSeries(series2)

            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }
        graphView.setTitle("Expenses")
        graphView2.setTitle("Entries")

    }
}