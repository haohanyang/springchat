from os import getenv
import pymssql

server = getenv("DEV_DATASOURCE_SERVER")
username = getenv("DEV_DATASOURCE_USERNAME")
password = getenv("DEV_DATASOURCE_PASSWORD")
database = "tempdb"

schema = "app"

conn = pymssql.connect("f", "Fs", "f", database)
cursor = conn.cursor(as_dict=True)

cursor.execute("CREATE SCHEMA " + schema)
cursor.execute("SELECT * FROM INFORMATION_SCHEMA.SCHEMATA")

schema_created = False

row = cursor.fetchone()
while row:
    if row["SCHEMA_NAME"] == schema:
        schema_created = True
    row = cursor.fetchone()
conn.close()

if not schema_created:
    print(SystemError, "Failed to create schema app")
