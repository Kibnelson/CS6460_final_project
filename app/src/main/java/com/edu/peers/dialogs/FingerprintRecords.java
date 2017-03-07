/*
 * DbRecord.java
 */

package com.edu.peers.dialogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Vector;


/**
 * This class represent a accessCredential fingerprint database record.
 */
public class FingerprintRecords {

  /**
   * User name
   */
  private String m_UserName;

  /**
   * User unique key
   */
  private byte[] m_Key;

  /**
   * Finger template.
   */
  private byte[] m_Template;

  /**
   * Creates a new instance of DbRecord class.
   */
  public FingerprintRecords() {
    m_UserName = "";
    // Generate accessCredential's unique identifier
    m_Key = new byte[16];
    java.util.UUID guid = java.util.UUID.randomUUID();
    long itemHigh = guid.getMostSignificantBits();
    long itemLow = guid.getLeastSignificantBits();
    for (int i = 7; i >= 0; i--) {
      m_Key[i] = (byte) (itemHigh & 0xFF);
      itemHigh >>>= 8;
      m_Key[8 + i] = (byte) (itemLow & 0xFF);
      itemLow >>>= 8;
    }
    m_Template = null;
  }

  /**
   * Initialize a new instance of DbRecord class from the file.
   *
   * @param szFileName a file name with previous saved passport.
   */
  public FingerprintRecords(String szFileName)
      throws FileNotFoundException, NullPointerException, AppException {
    Load(szFileName);
  }

  /**
   * Function read all records from database.
   *
   * @param szDbDir database folder
   * @return reference to Vector objects with records
   */
  static Vector<FingerprintRecords> ReadRecords(String szDbDir) {
    File DbDir;
    File[] files;
    Vector<FingerprintRecords> Users = new Vector<FingerprintRecords>(10, 10);

    // Read all records to identify
    DbDir = new File(szDbDir);
    files = DbDir.listFiles();

    if ((files == null) || (files.length == 0)) {
      return Users;
    }

    for (int iFiles = 0; iFiles < files.length; iFiles++) {
      try {
        if (files[iFiles].isFile()) {
          FingerprintRecords User = new FingerprintRecords(files[iFiles].getAbsolutePath());
          Users.add(User);
        }
      } catch (FileNotFoundException e) {
        // The record has invalid data. Skip it and continue processing.
      } catch (NullPointerException e) {
        // The record has invalid data. Skip it and continue processing.
      } catch (AppException e) {
        // The record has invalid data or access denied. Skip it and continue processing.
      }
    }

    return Users;
  }

  /**
   * Load accessCredential's information from file.
   *
   * @param szFileName a file name with previous saved passport.
   * @throws NullPointerException           szFileName parameter has null reference.
   * @throws java.io.InvalidObjectException the file has invalid structure.
   * @throws java.io.FileNotFoundException  the file not found or access denied.
   */
  private void Load(String szFileName)
      throws FileNotFoundException, NullPointerException, AppException {
    FileInputStream fs = null;
    File f = null;
    long nFileSize;

    f = new File(szFileName);
    if (!f.exists() || !f.canRead()) {
      throw new FileNotFoundException("File " + f.getPath());
    }

    try {
      nFileSize = f.length();
      fs = new FileInputStream(f);

      CharsetDecoder utf8Decoder = Charset.forName("UTF-8").newDecoder();
      byte[] Data = null;

      // Read accessCredential name length and accessCredential name in UTF8
      if (nFileSize < 2) {
        fs.close();
        throw new AppException("Bad file " + f.getPath());
      }
      int nLength = (fs.read() << 8) | fs.read();
      nFileSize -= 2;
      if (nFileSize < nLength) {
        fs.close();
        throw new AppException("Bad file " + f.getPath());
      }
      nFileSize -= nLength;
      Data = new byte[nLength];
      fs.read(Data);
      m_UserName = utf8Decoder.decode(ByteBuffer.wrap(Data)).toString();

      // Read accessCredential unique ID
      if (nFileSize < 16) {
        fs.close();
        throw new AppException("Bad file " + f.getPath());
      }
      nFileSize -= 16;
      m_Key = new byte[16];
      fs.read(m_Key);

      // Read template length and template data
      if (nFileSize < 2) {
        fs.close();
        throw new AppException("Bad file " + f.getPath());
      }

      nLength = (fs.read() << 8) | fs.read();
      nFileSize -= 2;
      if (nFileSize != nLength) {
        fs.close();
        throw new AppException("Bad file " + f.getPath());
      }
      m_Template = new byte[nLength];
      fs.read(m_Template);
      fs.close();
    } catch (SecurityException e) {
      throw new AppException("Denies read access to the file " + szFileName);
    } catch (IOException e) {
      throw new AppException("Bad file " + szFileName);
    }
  }

  /**
   * Save accessCredential's information to file.
   *
   * @param szFileName a file name to save.
   * @return true if passport successfully saved to file, otherwise false.
   * @throws NullPointerException  szFileName parameter has null reference.
   * @throws IllegalStateException some parameters are not set.
   * @throws java.io.IOException   can not create file or can not write data into file.
   */
  public boolean Save(String szFileName)
      throws NullPointerException, IllegalStateException, IOException {
    FileOutputStream fs = null;
    File f = null;
    boolean bRetcode = false;

    if (m_Template == null || m_UserName == null || m_UserName.length() == 0) {
      throw new IllegalStateException();
    }

    try {
      f = new File(szFileName);
      fs = new FileOutputStream(f);

      CharsetEncoder utf8Encoder = Charset.forName("UTF-8").newEncoder();
      byte[] Data = null;

      // Save accessCredential name
      ByteBuffer bBuffer = utf8Encoder.encode(CharBuffer.wrap(m_UserName.toCharArray()));
      Data = new byte[bBuffer.limit()];
      bBuffer.get(Data);
      fs.write(((Data.length >>> 8) & 0xFF));
      fs.write((Data.length & 0xFF));
      fs.write(Data);

      // Save accessCredential unique ID
      fs.write(m_Key);

      // Save accessCredential template
      fs.write(((m_Template.length >>> 8) & 0xFF));
      fs.write((m_Template.length & 0xFF));
      fs.write(m_Template);
      fs.close();
      bRetcode = true;
    } finally {
      if (!bRetcode && f != null) {
        f.delete();
      }
    }

    return bRetcode;
  }

  /**
   * Get the accessCredential name.
   */
  public String getUserName() {
    return m_UserName;
  }

  /**
   * Set the accessCredential name.
   */
  public void setUserName(String value) {
    m_UserName = value;
  }

  /**
   * Get the accessCredential template.
   */
  public byte[] getTemplate() {
    return m_Template;
  }

  /**
   * Set the accessCredential template.
   */
  public void setTemplate(byte[] value) {
    m_Template = value;
  }

  /**
   * Get the accessCredential unique identifier.
   */
  public byte[] getUniqueID() {
    return m_Key;
  }

}
