using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;
using System.IO;

namespace WindowsTagAnalyzer.com.fh.praktikanten.io
{
        /// <summary>
        /// Description of FileHasher.
        /// </summary>
        public class FileHasher
        {
            private static MD5 md5ServiceProvider = null;
            private static SHA1 sha1ServiceProvider = null;
            private static SHA256 sha256ServiceProvider = null;
            private static SHA384 sha384ServiceProvider = null;
            private static SHA512 sha512ServiceProvider = null;

            public static string getMD5(string parTheFilename)
            {
                try
                {
                    if (md5ServiceProvider == null)
                        md5ServiceProvider = new MD5CryptoServiceProvider();

                    FileStream fileStream = new FileStream(parTheFilename, FileMode.Open);
                    string hashValue = (BitConverter.ToString(md5ServiceProvider.ComputeHash(fileStream))).Replace("-", "");
                    fileStream.Close();
                    return hashValue;
                }
                catch (Exception e)
                {
                }
                return null;

            }

            public static string getSHA1(string parTheFilename)
            {
                try
                {
                    if (sha1ServiceProvider == null)
                        sha1ServiceProvider = new SHA1CryptoServiceProvider();

                    FileStream fileStream = new FileStream(parTheFilename, FileMode.Open);
                    string hashValue = (BitConverter.ToString(sha1ServiceProvider.ComputeHash(fileStream))).Replace("-", "");
                    fileStream.Close();
                    return hashValue;
                }
                catch (Exception e)
                {
                }
                return null;

            }

            public static string getSHA256(string parTheFilename)
            {
                try
                {
                    if (sha256ServiceProvider == null)
                        sha256ServiceProvider = new SHA256CryptoServiceProvider();

                    FileStream fileStream = new FileStream(parTheFilename, FileMode.Open);
                    string hashValue = (BitConverter.ToString(sha256ServiceProvider.ComputeHash(fileStream))).Replace("-", "");
                    fileStream.Close();
                    return hashValue;
                }
                catch (Exception e)
                {
                }
                return null;

            }

            public static string getSHA384(string parTheFilename)
            {
                try
                {
                    if (sha384ServiceProvider == null)
                        sha384ServiceProvider = new SHA384CryptoServiceProvider();

                    FileStream fileStream = new FileStream(parTheFilename, FileMode.Open);
                    string hashValue = (BitConverter.ToString(sha384ServiceProvider.ComputeHash(fileStream))).Replace("-", "");
                    fileStream.Close();
                    return hashValue;
                }
                catch (Exception e)
                {
                }
                return null;
            }


            public static string getSHA512(string parTheFilename)
            {
                try
                {
                    if (sha512ServiceProvider == null)
                        sha512ServiceProvider = new SHA512CryptoServiceProvider();

                    FileStream fileStream = new FileStream(parTheFilename, FileMode.Open);
                    string hashValue = (BitConverter.ToString(sha512ServiceProvider.ComputeHash(fileStream))).Replace("-", "");
                    fileStream.Close();
                    return hashValue;
                }
                catch (Exception e)
                {
                }
                return null;
            }
        }
    }
