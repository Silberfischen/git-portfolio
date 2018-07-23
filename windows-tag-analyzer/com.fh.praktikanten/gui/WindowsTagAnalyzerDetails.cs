using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using WindowsTagAnalyzer.com.fh.praktikanten.io;
using WindowsTagAnalyzer.com.fh.praktikanten.db;
using System.Text.RegularExpressions;

namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    public partial class WindowsTagAnalyzerDetails : Form
    {
        //TagReader for the file to be displayed
        TagReader currentTagReader = null;

        //List to store the values of the reasonable tags
        List<string> reasonableTagstoProof = new List<string>();

        regexGUI regex;

        public WindowsTagAnalyzerDetails()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Initialization of the GUI
        /// </summary>
        /// <param name="fileTagReader">The FileTagReader width the data of the file</param>
        public WindowsTagAnalyzerDetails(TagReader fileTagReader, regexGUI rx)
        {
            InitializeComponent();

            regex = rx;

            currentTagReader = fileTagReader;

            string path;
            path = fileTagReader.getAbsoluteFilePath();
           
            //Fill the labels width the name and the path of the file
            FileName.Text = ExtractFileName(path);
            FilePath.Text = path.Substring(0, path.Length - (ExtractFileName(path).Length));
            
            this.Visible = true;
        }

        /// <summary>
        /// Create the datatable for the output
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void WindowsTagAnalyzerDetails_Load(object sender, EventArgs e)
        {
            //Datatable for the output
            DataTable tagDataTable = new DataTable();
            Dictionary<string,string> usedTags = new Dictionary<string,string>();

            //Create columns
            tagDataTable.Columns.Add("Tag", typeof(String));
            tagDataTable.Columns.Add("Value", typeof(String));

            //Get all tags width a value
            usedTags = currentTagReader.getUsedTags();

            //Get all reasonable tags for the filetype
            reasonableTagstoProof = DAO.getDAO().getReasonableTags(ExtractFiletype(currentTagReader.getAbsoluteFilePath()));

            //Create rows
            foreach (KeyValuePair<string,string> kvp in usedTags)
            {
                tagDataTable.Rows.Add(kvp.Key, kvp.Value);
            }

            //set the datagridview to display the datatable we just created
            dataGridView1.ReadOnly = true;
            dataGridView1.DataSource = tagDataTable;

            //Checks every row if there are suspicious tags in it
            for (int index = 0; index < dataGridView1.Rows.Count; index++)
            {
                CheckTags(index);
                foreach (Object reg in regex.clb.Items)
                {
                    if(proofRegex(index, reg.ToString()))
                    {
                        dataGridView1.Rows[index].DefaultCellStyle.BackColor = Color.Orange;
                    }
                }
            }

            dataGridView1.Columns[0].Width = 350;
            dataGridView1.Columns[1].Width = 408;
        }

        private Boolean proofRegex(int index, String regex)
        {

            //checks if the URL regex is checked in the regexGUI
            if (regex.Equals("URL"))
            {
                string pattern = @"((https?|ftp|gopher|telnet|file|notes|ms-help):((//)|(\\\\))+[\w\d:#@%/;$()~_?\+-=\\\.&]*)";

                Regex reg = new Regex(pattern);

                //searchs for the regex in the tag value


                if (reg.IsMatch(dataGridView1.Rows[index].Cells[1].Value.ToString()))
                {
                    return true;
                }

            }
            else if (regex.Equals("Base64"))
            {
                string pattern = @"[0-9a-zA-Z\+/=]{20,}";

                Regex reg = new Regex(pattern);

                if (reg.IsMatch(dataGridView1.Rows[index].Cells[1].Value.ToString()))
                {
                    return true;
                }
            }
            else
            {
                Regex reg = null;

                reg = new Regex(@"" + regex);

                if (reg.IsMatch(dataGridView1.Rows[index].Cells[1].Value.ToString()))
                {
                    return true;
                }
            }

            return false;
        }

        /// <summary>
        /// Extracts the file name from the rest of the path
        /// </summary>
        /// <param name="path">The complete path of the file width the filename</param>
        /// <returns>The name of the file without the path</returns>
        private string ExtractFileName(string path)
        {
            string folderName = null;

            for (int i = path.Length - 1; i > 0; i--)
            {
                //read the name backwards till "\"
                folderName = path[i] + folderName;
                if (("" + path[i - 1]) == "\\")
                    return folderName;
            }

            return null;
        }

        /// <summary>
        /// Extracts the filetype from the path
        /// </summary>
        /// <param name="path">the path of the file</param>
        /// <returns>the filytype</returns>
        private string ExtractFiletype(string path)
        {
            string type = null;

            for (int i = path.Length - 1; i > 0; i--)
            {
                type = path[i] + type;
                if((path[i - 1]) == '.')
                    return type;
            }

            return null;
        }

        /// <summary>
        /// Checks if the row contains a suspicious tag
        /// if the tag is suspicious the row is marked red
        /// </summary>
        /// <param name="index">the index of the row that has to be checked</param>
        private void CheckTags(int index)
        {
            if (!reasonableTagstoProof.Contains(dataGridView1.Rows[index].Cells[0].Value))
            {
                dataGridView1.Rows[index].DefaultCellStyle.BackColor = Color.Red;
            }
        }

        /// <summary>
        /// if the user sorts the datagridview the datagridview has to be updated 
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void dataGridView1_Sorted(object sender, EventArgs e)
        {
            //updates the rows and marks the suspicious tags red
            for (int index = 0; index < dataGridView1.Rows.Count; index++)
            {
                CheckTags(index);
            }
        }
    }
}
