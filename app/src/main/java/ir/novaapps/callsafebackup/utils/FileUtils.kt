package ir.novaapps.callsafebackup.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FileUtils {

    companion object{
        fun clearFolder(folder: File): Boolean {
            if (folder.exists() && folder.isDirectory) {
                folder.listFiles()?.forEach { file ->
                    if (file.isDirectory) {
                        // پاک کردن محتویات زیرپوشه به صورت بازگشتی
                        clearFolder(file)
                    }
                    // حذف فایل یا پوشه
                    file.delete()
                }
            }
            return folder.listFiles()?.isEmpty() ?: true // بررسی اینکه پوشه خالی شده باشد
        }

        fun zipSingleFolder(context: Context, inputPath: String?, outputZipDirName: String, zipName: String) {
            var outputZipDirName = outputZipDirName
            val inputDir = File(inputPath)
            if (!inputDir.absolutePath.startsWith(context.dataDir.absolutePath) && !outputZipDirName.startsWith(
                    context.dataDir.absolutePath
                )
            ) {
                outputZipDirName = "$outputZipDirName/$zipName.zip"
                FileOutputStream(outputZipDirName).use { fileOutputStream ->
                    ZipOutputStream(fileOutputStream).use { zipOutputStream ->
                        addToZipForFolder(inputDir, inputDir.name, zipOutputStream)
                    }
                }
                Log.d("TAG_APP", "one folder compressed!")
            } else {
                outputZipDirName = "$outputZipDirName/$zipName.zip"
                FileOutputStream(outputZipDirName).use { fileOutputStream ->
                    ZipOutputStream(fileOutputStream).use { zipOutputStream ->
                        addToZipForFolder(inputDir, inputDir.name, zipOutputStream)
                    }
                }
                Log.d("TAG_APP", "one folder compressed!")
            }
        }


        private fun addToZipForFolder(
            folderToZip: File,
            fileName: String,
            zipOutputStream: ZipOutputStream
        ) {
            if (folderToZip.isHidden) return

            if (folderToZip.exists() && folderToZip.isDirectory) {
                if (fileName.endsWith("/")) {
                    zipOutputStream.putNextEntry(ZipEntry(fileName))
                    zipOutputStream.closeEntry()
                } else {
                    zipOutputStream.putNextEntry(ZipEntry("$fileName/"))
                    zipOutputStream.closeEntry()
                }

                val children = folderToZip.listFiles()
                for (child in children) addToZipForFolder(
                    child,
                    fileName + "/" + child.name,
                    zipOutputStream
                )
                return
            }

            FileInputStream(folderToZip).use { fileInputStream ->
                val zipEntry = ZipEntry(fileName)
                zipOutputStream.putNextEntry(zipEntry)

                val bytes = ByteArray(2048)
                var length: Int
                while ((fileInputStream.read(bytes).also { length = it }) >= 0) {
                    zipOutputStream.write(bytes, 0, length)
                }
            }
        }
    }
}