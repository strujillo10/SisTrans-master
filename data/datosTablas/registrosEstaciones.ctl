load data 
infile 'registrosEstaciones.csv' "str '\r\n'"
append
into table REGISTROSCOMPONENTES
fields terminated by ','
OPTIONALLY ENCLOSED BY '"' AND '"'
trailing nullcols
           ( ID CHAR(4000),
             IDCOMPONENTE CHAR(4000),
             ID CHAR(4000),
             ID CHAR(4000)
           )
