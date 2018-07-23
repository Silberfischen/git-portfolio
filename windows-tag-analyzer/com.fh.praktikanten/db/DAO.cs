using System.Collections.Generic;
using System.Data.SQLite;
using WindowsTagAnalyzer.com.fh.praktikanten.io;
using System.Data;
using System.IO;

namespace WindowsTagAnalyzer.com.fh.praktikanten.db
{
    /// <summary>
    /// The DAO class is for all sorts of pre-defined db-operations
    /// </summary>
    class DAO
    {

        /// <summary>
        /// DAO object for the Singleton-Pattern 
        /// </summary>
        private static DAO singletonDAO;

        /// <summary>
        /// The Connection to the SQLite database
        /// </summary>
        private SQLiteConnection conn;

        /// <summary>
        /// The path to the SQLite database
        /// </summary>
        private static readonly string DB_PATH = "wta.sqlite";

        private static readonly string RENEW_DB_PATH = " ..\\..\\sqlScripts\\All.sql";
        /// <summary>
        /// The private DAO constructor which establishes the connection to the db
        /// </summary>
        private DAO()
        {
            //initialize the db connection
            conn = new SQLiteConnection("Data Source=" + DAO.DB_PATH);

            //open the db connection
            conn.Open();
        }

        /// <summary>
        /// The only function to retrieve the DAO object to read and manipulate the db
        /// </summary>
        /// <returns>The only DAO object</returns>
        public static DAO getDAO()
        {

            if (DAO.singletonDAO == null)
            {
                //Create the singleton object
                DAO.singletonDAO = new DAO();
            }

            return DAO.singletonDAO;
        }

        /// <summary>
        /// Returns all the tags from a filetype which are considered reasonable
        /// </summary>
        /// <param name="filetype">The filetype of a file as a string</param>
        /// <returns>The reasonable tags as a generic list of string</returns>
        public List<string> getReasonableTags(string filetype)
        {
            //Create query object
            SQLiteCommand query = new SQLiteCommand(conn);
            //Create Parameter
            SQLiteParameter param = new SQLiteParameter();
            //The list which will be returned
            List<string> fileTypes = new List<string>();

            //The query which will be executed
            //It returns the 5 global tags which every file can have
            //and the file-specific ones stored in the db
            query.CommandText = "SELECT TagName FROM Tags WHERE TagID IN " +
                                    "(SELECT TagID FROM ReasonableTags WHERE FileTypeID IN " +
                                        "(SELECT FileTypeID FROM FileTypes WHERE FileTypeName = @fileTypeName)) " +
                                "UNION " +
                                "SELECT TagName FROM Tags WHERE TagID IN " +
                                    "(SELECT TagID FROM ReasonableTags WHERE FileTypeID = -1);";


            using (SQLiteCommand command = new SQLiteCommand(query.CommandText, conn))
            {
                //Yet another param-adding
                command.Parameters.AddWithValue("@fileTypeName", filetype.ToLower());

                //Executing the query
                using (SQLiteDataReader dr = command.ExecuteReader())
                {
                    //Iterate through the result-set
                    while (dr.Read())
                    {
                        //Add a FileTag to the list
                        fileTypes.Add((string)dr.GetValue(0));
                    }
                }
            }
            return fileTypes;
        }

        /// <summary>
        /// Returns all tags specified in the database
        /// </summary>
        /// <returns>The tags as a generic list of string</returns>
        public List<string> getAllTags()
        {
            //Create query object
            SQLiteCommand query = new SQLiteCommand(conn);
            //The list which will be returned
            List<string> fileTypes = new List<string>();

            //The query which will be executed
            query.CommandText = "SELECT TagName FROM Tags";

            using (SQLiteCommand command = new SQLiteCommand(query.CommandText, conn))
            {
                //Executing the query
                using (SQLiteDataReader dr = command.ExecuteReader())
                {
                    //Read through the result-set
                    while (dr.Read())
                    {
                        //Add a FileTag to the list
                        fileTypes.Add((string)dr.GetValue(0));
                    }
                }
            }
            return fileTypes;
        }

        /// <summary>
        /// Deletes the Tags from the db and stores the new tags from the dictionary in the db
        /// </summary>
        /// <param name="tags">The Dictionary must contain the params in form of: "TagID,TagName"</param>
        public void renewAllTags(Dictionary<int, string> tags)
        {
            //Create query object
            SQLiteCommand query = new SQLiteCommand(conn);

            //Begin transaction
            SQLiteTransaction tx = conn.BeginTransaction();

            //The query which will be executed
            query.CommandText = "DELETE FROM Tags;";

            //Delete everything!
            query.ExecuteNonQuery();

            foreach (KeyValuePair<int, string> kvp in tags)
            {
                //Save all tags in this format : "1,Name"
                query.CommandText = "INSERT INTO Tags VALUES(@tagID,@tagName);";

                //Add params
                query.Parameters.AddWithValue("@tagID", kvp.Key);
                query.Parameters.AddWithValue("@tagName", kvp.Value);

                //Execute inserts
                query.ExecuteNonQuery();
            }

            //Commit Transactions
            tx.Commit();

        }

        public void renewWholeDB()
        {
            //Create query object
            SQLiteCommand query = new SQLiteCommand(conn);

            //Delete the reasonable tags with specified TagName and FileTypeName
            query.CommandText = "SOURCE @path";

            //Add params to the statement
            query.Parameters.AddWithValue("@path", DAO.RENEW_DB_PATH);

            //Execute query after query
            query.ExecuteNonQuery();

        }

        /// <summary>
        /// Inserts all the given Reasonable Tags into the db
        /// </summary>
        /// <param name="relations">The Dictionary must contain the params in form of: "TagName,FileTypeName"</param>
        public void insertReasonableTags(Dictionary<string, string> relations)
        {
            //Create query object
            SQLiteCommand query = new SQLiteCommand(conn);

            SQLiteTransaction tx = conn.BeginTransaction();

            foreach (KeyValuePair<string, string> kvp in relations)
            {
                //The query which will be executed
                query.CommandText = "INSERT INTO ReasonableTags " +
                                    "SELECT TagID,FileTypeID FROM Tags JOIN FileTypes WHERE TagName = @tagName " +
                                    "AND FileTypeName = @fileTypeName";

                query.Parameters.AddWithValue("@tagName", kvp.Key);
                query.Parameters.AddWithValue("@fileTypeName", kvp.Value);

                //Execute query after query
                query.ExecuteNonQuery();
            }

            //Commit the transaction
            tx.Commit();
        }

        /// <summary>
        /// Deletes the given reasonable tags from the db
        /// </summary>
        /// <param name="relations">The Dictionary must contain the params in form of: "TagName,FileTypeName"</param>
        public void deleteReasonableTags(Dictionary<string, string> relations)
        {
            //Create query object
            SQLiteCommand query = new SQLiteCommand(conn);

            SQLiteTransaction tx = conn.BeginTransaction();

            foreach (KeyValuePair<string, string> kvp in relations)
            {
                //Delete the reasonable tags with specified TagName and FileTypeName
                query.CommandText = "DELETE FROM ReasonableTags WHERE TagID IN" +
                                    "(SELECT TagID FROM Tags WHERE TagName = @tagName) AND FileTypeID IN " +
                                    "(SELECT FileTypeID FROM FileTypes WHERE FileTypeName = @fileTypeName)";

                //Add params to the statement
                query.Parameters.AddWithValue("@tagName", kvp.Key);
                query.Parameters.AddWithValue("@fileTypeName", kvp.Value);

                //Execute query after query
                query.ExecuteNonQuery();
            }

            //Commit the transaction
            tx.Commit();
        }

        /// <summary>
        /// Saves all the needed infos about the File into the db
        /// </summary>
        /// <param name="tr">A TagReader object which contains all the infos about the File's tags</param>
        public void saveReportParams(TagReader tr)
        {
            //Create query object
            SQLiteCommand query = new SQLiteCommand(conn);

            //Begin transaction
            SQLiteTransaction tx = conn.BeginTransaction();

            //Delete old entries of the file
            query.CommandText = "DELETE FROM Reports WHERE FilePath = '" + tr.getAbsoluteFilePath() + "';";

            //send the delete query to the server
            query.ExecuteNonQuery();

            foreach (KeyValuePair<string, string> kvp in tr.getUsedTags())
            {
                //The query which will be executed
                query.CommandText = "INSERT INTO Reports VALUES(@absolutePath," +
                                    "(SELECT TagID FROM Tags WHERE TagName = @tagName)," +
                                    "@tagValue," +
                                    "@sha512," +
                                    "@sha384," +
                                    "@sha256," +
                                    "@sha1," +
                                    "@md5);";

                //Add parameters to the prepared statement
                query.Parameters.AddWithValue("@absolutePath", tr.getAbsoluteFilePath());
                query.Parameters.AddWithValue("@tagName", kvp.Key);
                query.Parameters.AddWithValue("@tagValue", kvp.Value);

                //Add hashes to the statement
                foreach (KeyValuePair<string, string> hash in tr.getHashes())
                {
                    query.Parameters.AddWithValue("@" + hash.Key, hash.Value);
                }

                //Execute query after query
                query.ExecuteNonQuery();
            }

            //Commit the transaction
            tx.Commit();
        }

        /// <summary>
        /// Read the report statements out of the db and writes them to the given path
        /// </summary>
        /// <param name="pathToReport">The path of the folder of which the report should be executed</param>
        /// <param name="pathToWrite">The path of the report file (.csv)</param>
        public void writeReport(string pathToReport, string pathToWrite)
        {
            //Create query object
            SQLiteCommand query = new SQLiteCommand(conn);

            //The query which will be executed
            query.CommandText = "SELECT FilePath," +
                                "(SELECT TagName From Tags WHERE TagID = Reports.TagID), " +
                                "TagValue, " +
                                "SHA512," +
                                "SHA384," +
                                "SHA256," +
                                "SHA1," +
                                "MD5 " +
                                "FROM Reports WHERE FilePath LIKE @filePath";

            using (SQLiteCommand command = new SQLiteCommand(query.CommandText, conn))
            {
                //Add params
                command.Parameters.AddWithValue("@filePath", pathToReport + "%");

                //Executing the query
                using (SQLiteDataReader dr = command.ExecuteReader())
                {
                    //Directly write to the file
                    using (StreamWriter writer = new StreamWriter(pathToWrite))
                    {
                        writer.WriteLine(
                               "File Path" + ";" + "Tag name" + ";" +
                               "Tag value" + ";" + "SHA512" + ";" +
                               "SHA384" + ";" + "SHA256" + ";" +
                              "SHA1" + ";" + "MD5");

                        //Read through the result-set
                        while (dr.Read())
                        {
                            string[] valueParam = new string[5];

                            //if the string is empty it's replaced with NULL
                            for (int i = 0; i < 5; i++)
                            {
                                if ("".Equals(dr.GetValue(i + 3).ToString()))
                                {
                                    valueParam[i] = "NULL";
                                }
                                else
                                {
                                    valueParam[i] = dr.GetValue(i + 3).ToString();
                                }
                            }

                            //Write in .csv format to the file
                            writer.WriteLine(
                                dr.GetValue(0).ToString() + ";" + dr.GetValue(1).ToString() + ";" +
                                dr.GetValue(2).ToString() + ";" + valueParam[0] + ";" +
                                valueParam[1] + ";" + valueParam[2] + ";" +
                               valueParam[3] + ";" + valueParam[4]);
                        }
                    }
                }
            }
        }
    }
}