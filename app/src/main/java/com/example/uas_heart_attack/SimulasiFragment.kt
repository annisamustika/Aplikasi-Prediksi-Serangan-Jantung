package com.example.uas_heart_attack

import android.content.Context
import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.uas_heart_attack.R
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class SimulasiFragment : Fragment() {

    private lateinit var interpreter: Interpreter
    private val mModelPath = "heart.tflite"

    private lateinit var resultText: TextView
    private lateinit var editAge: EditText
    private lateinit var editSex: EditText
    private lateinit var editCp: EditText
    private lateinit var editTrtbps: EditText
    private lateinit var editChol: EditText
    private lateinit var editFbs: EditText
    private lateinit var editRestecg: EditText
    private lateinit var editThalachh: EditText
    private lateinit var editExng: EditText
    private lateinit var editOldpeak: EditText
    private lateinit var editSlp: EditText
    private lateinit var editCaa: EditText
    private lateinit var editThall: EditText
    private lateinit var checkButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simulasi, container, false)

        resultText = view.findViewById(R.id.txtResult)
        editAge = view.findViewById(R.id.editAge)
        editSex = view.findViewById(R.id.editSex)
        editCp = view.findViewById(R.id.editCp)
        editTrtbps = view.findViewById(R.id.editTrtbps)
        editChol = view.findViewById(R.id.editChol)
        editFbs = view.findViewById(R.id.editFbs)
        editRestecg = view.findViewById(R.id.editRestecg)
        editThalachh = view.findViewById(R.id.editThalachh)
        editExng = view.findViewById(R.id.editExng)
        editOldpeak = view.findViewById(R.id.editOldpeak)
        editSlp = view.findViewById(R.id.editSlp)
        editCaa = view.findViewById(R.id.editCaa)
        editThall = view.findViewById(R.id.editThall)
        checkButton = view.findViewById(R.id.btnCheck)

        checkButton.setOnClickListener {
            if (isValidInput()) {
                val result = doInference(
                    editAge.text.toString(),
                    editSex.text.toString(),
                    editCp.text.toString(),
                    editTrtbps.text.toString(),
                    editChol.text.toString(),
                    editFbs.text.toString(),
                    editRestecg.text.toString(),
                    editThalachh.text.toString(),
                    editExng.text.toString(),
                    editOldpeak.text.toString(),
                    editSlp.text.toString(),
                    editCaa.text.toString(),
                    editThall.text.toString()
                )

                if (result == 1) {
                    resultText.text = "Terkena serangan jantung"
                } else {
                    resultText.text = "Tidak terkena serangan jantung"
                }
            } else {
                Toast.makeText(context, "Harap lengkapi semua inputan", Toast.LENGTH_SHORT).show()
            }
        }

        initInterpreter()

        return view
    }

    private fun isValidInput(): Boolean {
        return editAge.text.isNotBlank() &&
                editSex.text.isNotBlank() &&
                editCp.text.isNotBlank() &&
                editTrtbps.text.isNotBlank() &&
                editChol.text.isNotBlank() &&
                editFbs.text.isNotBlank() &&
                editRestecg.text.isNotBlank() &&
                editThalachh.text.isNotBlank() &&
                editExng.text.isNotBlank() &&
                editOldpeak.text.isNotBlank() &&
                editSlp.text.isNotBlank() &&
                editCaa.text.isNotBlank() &&
                editThall.text.isNotBlank()
    }

    private fun initInterpreter() {
        val options = Interpreter.Options()
        options.setNumThreads(5)
        options.setUseNNAPI(true)
        interpreter = Interpreter(loadModelFile(requireContext().assets, mModelPath), options)
    }

    private fun doInference(
        age: String, sex: String, cp: String, trtbps: String, chol: String, fbs: String,
        restecg: String, thalachh: String, exng: String, oldpeak: String, slp: String,
        caa: String, thall: String
    ): Int {
        val inputVal = FloatArray(13)
        inputVal[0] = age.toFloat()
        inputVal[1] = sex.toFloat()
        inputVal[2] = cp.toFloat()
        inputVal[3] = trtbps.toFloat()
        inputVal[4] = chol.toFloat()
        inputVal[5] = fbs.toFloat()
        inputVal[6] = restecg.toFloat()
        inputVal[7] = thalachh.toFloat()
        inputVal[8] = exng.toFloat()
        inputVal[9] = oldpeak.toFloat()
        inputVal[10] = slp.toFloat()
        inputVal[11] = caa.toFloat()
        inputVal[12] = thall.toFloat()

        val output = Array(1) { FloatArray(2) }
        interpreter.run(inputVal, output)
        Log.e("result", output[0].contentToString())

        // Menentukan hasil prediksi berdasarkan output
        return if (output[0][0] > output[0][1]) 0 else 1
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
