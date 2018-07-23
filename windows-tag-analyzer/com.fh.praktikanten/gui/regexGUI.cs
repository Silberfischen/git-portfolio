using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace WindowsTagAnalyzer.com.fh.praktikanten.gui
{
    public partial class regexGUI : Form
    {
        
        public CheckedListBox clb = new CheckedListBox();

        public regexGUI()
        {
            InitializeComponent();
            clb = regexlist;
        }

        private void regexGUI_Load(object sender, EventArgs e)
        {
           clb.CheckOnClick = true;
        }

        private void done_Click(object sender, EventArgs e)
        {
            this.Hide();
        }

        private void regexGUI_FormClosing(object sender, FormClosingEventArgs e)
        {
            this.Hide();
        }

        private void addOwn_Click(object sender, EventArgs e)
        {
            addRegexGUI arg = new addRegexGUI(this);
            arg.Show();
            
            
        }
    }
}
