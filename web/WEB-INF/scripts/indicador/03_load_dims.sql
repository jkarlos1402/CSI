/********************* INDICADORES DCS  **************************/


/****** CARGA DE CATALOGO PAIS **********/

MERGE INTO DIM_PAISES D
USING (SELECT DISTINCT PAIS FROM XTMPINDDL_TMP WHERE NOT PAIS IS NULL)ST 
ON (D.GV_PAIS=ST.PAIS)
WHEN NOT MATCHED THEN
INSERT(PK_PAIS, GV_PAIS)
VALUES(SEQ_PK_DIM_PAISES.NEXTVAL,ST.PAIS);
COMMIT;

/****** CARGA DE CATALOGO PLANTAS **********/

MERGE INTO DIM_PLANTAS D
USING (SELECT DISTINCT CENTRO FROM XTMPINDDL_TMP WHERE NOT CENTRO IS NULL)ST 
ON (D.GV_PLANTA=ST.CENTRO)
WHEN NOT MATCHED THEN
INSERT(PK_PLANTA, GV_PLANTA)
VALUES(SEQ_PK_DIM_PLANTAS.NEXTVAL,ST.CENTRO);

COMMIT;

/****** CARGA DE CATALOGO LINEAS **********/

MERGE INTO DIM_RUTAS D
USING (SELECT DISTINCT RUTA FROM XTMPINDDL_TMP WHERE NOT RUTA IS NULL)ST 
ON (D.GV_RUTA=ST.RUTA)
WHEN NOT MATCHED THEN
INSERT(PK_RUTA, GV_RUTA)
VALUES(SEQ_PK_DIM_RUTAS.NEXTVAL,ST.RUTA);

COMMIT;

/****** CARGA DE CATALOGO TIPO DE DATOS **********/

MERGE INTO DIM_TIPO_DATOS D
USING (SELECT DISTINCT TIPO FROM XINDDL_ST_FACT WHERE NOT TIPO IS NULL)ST 
ON (D.GV_TIPO_DATO=ST.TIPO)
WHEN NOT MATCHED THEN
INSERT(PK_TIPO, GV_TIPO_DATO)
VALUES(SEQ_PK_DIM_TIPO_DATOS.NEXTVAL,ST.TIPO);

COMMIT;

/****** CARGA DE CATALOGO NIVEL INFORMACION **********/

MERGE INTO DIM_NIVEL_INFO D
USING (SELECT DISTINCT NIVEL FROM XINDDL_ST_FACT WHERE NOT NIVEL IS NULL)ST 
ON (D.GV_NIVEL=ST.NIVEL)
WHEN NOT MATCHED THEN
INSERT(PK_NIVEL, GV_NIVEL)
VALUES(SEQ_PK_DIM_NIVEL_INFO.NEXTVAL,ST.NIVEL);
COMMIT;

