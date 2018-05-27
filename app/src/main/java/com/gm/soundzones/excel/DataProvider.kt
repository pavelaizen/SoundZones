package com.gm.soundzones.excel

import android.content.Context
import android.os.Environment
import com.gm.soundzones.model.SoundRun
import com.gm.soundzones.model.SoundSet
import com.gm.soundzones.model.SoundTrack
import com.gm.soundzones.model.User
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


object DataProvider {
    private const val NOISE_SUFFIX = "_noise"
    private const val EXCEL_NAME = "tablet_input.xlsx"
    private lateinit var sheet: Sheet
    private lateinit var formulaEvaluator: FormulaEvaluator
    private val TOTAL_RUNS = 5
    private val SETS_IN_RUN = 9
    private val SOUND_SET_ROWS = 5
    private val CELLS_IN_RUN = SOUND_SET_ROWS * SETS_IN_RUN + 1 // +1 for run# column
    private var workbook: Workbook? = null
    val excelFile = File(Environment.getExternalStorageDirectory(), "output.xlsx")

    private val defaultVolumeLevels = HashMap<String, Int>()
    fun setDefaultVolume(dirName: String, hasNoise: Boolean, volume: Int) =
            defaultVolumeLevels.put(if (hasNoise) dirName.plus(NOISE_SUFFIX) else dirName, volume)

    fun getDefaultVolume(dirName: String, hasNoise: Boolean): Int? =
            defaultVolumeLevels[if (hasNoise) dirName.plus(NOISE_SUFFIX) else dirName]


    fun setup(context: Context) {
        (excelFile.takeIf { excelFile.exists() }?.inputStream() ?: context.assets.open(EXCEL_NAME)).also { stream ->
            workbook = XSSFWorkbook(stream).also {
                sheet = it.getSheetAt(0)
                formulaEvaluator = it.creationHelper.createFormulaEvaluator()
            }
            stream.close()
        }
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


    fun applyVolumeAccept(id: Int, runId: String, setIndex: Int, volume: Int) {
        sheet.getRow(id)?.let { row ->
            row.find { it.cellType == CELL_TYPE_STRING && it.stringCellValue == runId }?.columnIndex?.let {
                val volumeCellIndex = it + ((setIndex + 1) * SOUND_SET_ROWS) - 1
                row.getCell(volumeCellIndex)?.setCellValue(volume.toString()).also {
                    saveToFile()
                }
            }
        }
    }

    fun applyVolumeGreat(id: Int, runId: String, setIndex: Int, volume: Int) {
        sheet.getRow(id)?.let { row ->
            row.find { it.cellType == CELL_TYPE_STRING && it.stringCellValue == runId }?.columnIndex?.let {
                val volumeCellIndex = it + ((setIndex + 1) * SOUND_SET_ROWS)
                row.getCell(volumeCellIndex)?.setCellValue(volume.toString()).also {
                    saveToFile()
                }
            }
        }
    }

    private val lock = Mutex()
    private fun saveToFile() {
        launch(CommonPool) {
            lock.withLock {
                excelFile.takeUnless { it.exists() }?.createNewFile()
                val os = excelFile.outputStream()
                workbook?.write(os)
                os.close()
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