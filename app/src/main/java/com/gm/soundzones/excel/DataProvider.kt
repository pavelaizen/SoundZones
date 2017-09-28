package com.gm.soundzones.excel

import android.os.Environment
import com.gm.soundzones.log
import com.gm.soundzones.model.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by titan on 16-Sep-17.
 */
object DataProvider {
    const val EXCEL_NAME = "tablet_input.xlsx"
    private lateinit var sheet: Sheet
    private lateinit var formulaEvaluator: FormulaEvaluator
    private val TOTAL_RUNS = 5
    private val SETS_IN_RUN = 9
    private val SOUND_SET_ROWS = 5
    private val CELLS_IN_RUN = SOUND_SET_ROWS * SETS_IN_RUN + 1 // +1 for run# column
    private var workbook: Workbook? = null
    private val excelFile= File(Environment.getExternalStorageDirectory(),"output.xlsx")

    val defaultVolumeLevels = HashMap<String,Int>()

    fun setup(excelStream: InputStream) {
        workbook = XSSFWorkbook(excelStream).also {
            sheet = it.getSheetAt(0)
            formulaEvaluator = it.creationHelper.createFormulaEvaluator()

        }
        excelStream.close()

//        workbook?.getSheetAt(0)?.getRow(0)?.getCell(15)?.setCellValue("123")

    }


    fun getUser(id: Int): User =
            sheet.getRow(id).let {
                User(getCellAsString(it, 1, formulaEvaluator).toDouble().toInt(),
                        Array<SoundRun>(TOTAL_RUNS) { index ->
                            val position = index * CELLS_IN_RUN + 2
                            collectSoundRun(it, formulaEvaluator, position)
                        })
            }


    fun setVolumeAccept(id:Int,run:Int,volume:Int){
        sheet.getRow(id).getCell(2+run* SOUND_SET_ROWS-1).setCellValue(volume.toString())
        saveToFile()
    }
    fun setVolumeGreat(id:Int,run:Int,volume:Int){
        sheet.getRow(id).getCell(2+run* SOUND_SET_ROWS).setCellValue(volume.toString())
        saveToFile()
    }
    private val lock=Mutex()
    private fun saveToFile(){
        launch(CommonPool){
            lock.withLock {
                excelFile.takeUnless { it.exists() }?.createNewFile()
                workbook?.write(excelFile.outputStream())
            }
        }
    }
    private fun collectSoundRun(row: Row, formulaEvaluator: FormulaEvaluator, cellNumber: Int): SoundRun {
        val soundSets = Array<SoundSet>(SETS_IN_RUN) {
            val position = cellNumber.inc() + (it * SOUND_SET_ROWS)
            collectSoundSet(row, formulaEvaluator, position)
        }
        val runId = getCellAsString(row, cellNumber, formulaEvaluator)
        return SoundRun(runId, soundSets)
    }

    private fun collectSoundSet(row: Row, formulaEvaluator: FormulaEvaluator, startingCell: Int): SoundSet {
        val pair = getCellAsString(row, startingCell, formulaEvaluator)
        val primary = getCellAsString(row, startingCell + 1, formulaEvaluator)
        val secondary = getCellAsString(row, startingCell + 2, formulaEvaluator)
        return SoundSet(pair, SoundTrack(primary), SoundTrack(secondary))
    }

    private fun getCellAsString(row: Row, c: Int, formulaEvaluator: FormulaEvaluator) =
            row.getCell(c).let { cell ->
                formulaEvaluator.evaluate(cell)?.let { cellValue ->
                    when (cellValue.cellType) {
                        Cell.CELL_TYPE_BOOLEAN -> cellValue.booleanValue.toString()
                        Cell.CELL_TYPE_NUMERIC ->
                            SimpleDateFormat("dd/MM/yy", Locale.US)
                                    .takeIf { HSSFDateUtil.isCellDateFormatted(cell) }?.format(HSSFDateUtil.getJavaDate(cellValue.numberValue))
                                    ?: cellValue.numberValue.toString()

                        Cell.CELL_TYPE_STRING -> cellValue.stringValue
                        else -> cellValue.stringValue
                    }
                } ?: ""
            }
}