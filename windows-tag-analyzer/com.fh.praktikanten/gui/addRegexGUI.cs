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
    public partial class addRegexGUI : Form
    {
        private regexGUI reg = new regexGUI();
        public addRegexGUI(regexGUI rg)
        {
            InitializeComponent();
            reg = rg;
        }

        private void confirm_Click(object sender, EventArgs e)
        {
            reg.clb.Items.Add(regextextbox.Text);
            this.Hide();
        }
    }
}
