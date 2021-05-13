import sqlite3
from sqlite3 import Error

def convertToBinaryData(filename):
    with open(filename, 'rb') as file:
        blobData = file.read()
    return blobData

def create_connection(db_file):
    conn = None

    try:
        conn = sqlite3.connect(db_file)
    except Error as e:
        print(e)

    return conn

def new_transaction(dbPath, plate):
    conn = create_connection(dbPath)
    
    sql = ''' INSERT INTO "transaction" (carPhoto, platePhoto, plateNumber, entranceTimestamp, exitTimestamp, pricePerHour, employeeName)
    values (?, ?, ?, ?, ?, ?, ?); '''

    data_tuple = (
        convertToBinaryData(plate.carPhotoPath), 
        convertToBinaryData(plate.platePhotoPath), 
        plate.plateNumber, 
        plate.enteranceTimestamp, 
        plate.exitTimestamp, 
        plate.pricePerHour, 
        plate.employeeName
    ) 
    
    cur = conn.cursor()
    cur.execute(sql, data_tuple)
    conn.commit()

    return cur.lastrowid
