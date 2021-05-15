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
        print("CONN ERR:" + e)

    return conn

def new_transaction(dbPath, plate):
    conn = create_connection(dbPath)
    
    sql = ''' INSERT INTO "transaction" (carPhoto, platePhoto, plateNumber, entranceTimestamp, exitTimestamp, pricePerHour, employeeName)
    values (?, ?, ?, ?, ?, ?, ?); '''

    cur = conn.cursor()
    cur.execute(sql, plate)
    conn.commit()

    return cur.lastrowid

def get_transaction_car_photo(dbPath, id):
    conn = create_connection(dbPath)

    sql = ''' SELECT * FROM "transaction" WHERE id == ''' + id

    cur = conn.cursor()

    return cur.execute(sql).fetchone()[1]