using System;
using System.Collections.Generic;
using System.IO;
using System.Windows.Forms;
using WindowsTagAnalyzer.com.fh.praktikanten.gui;
using WindowsTagAnalyzer.com.fh.praktikanten.io;
using WindowsTagAnalyzer.com.fh.praktikanten.db;

namespace WindowsTagAnalyzer
{
    static class Program
    {
        /// <summary>
        /// Der Haupteinstiegspunkt für die Anwendung.
        /// </summary>
        [STAThread]
        static void Main()
        {

            //DAO.getDAO().renewAllTags(WindowsTagAnalyzer.com.fh.praktikanten.io.TagReader.getAllTags());

            //List<TagReader> test = TagReader.getAllTagReadersFromDirectory(new DirectoryInfo("C:\\Users\\michael\\"));

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new WindowsTagAnalyzerGUI());


        }
    }
}
