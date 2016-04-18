
/************ BORRA TABLA TEMPORAL ********************/

DROP TABLE XTMP_FLAGALERTA;
DROP TABLE XTMP_MAIL;
DROP TABLE XTMP_MENSAJE;


/************ CREACION DE TABLAS TEMPORALES ********************/

   CREATE TABLE XTMP_MAIL(
   PAIS VARCHAR(10),
   MAIL VARCHAR(50));
   
   CREATE TABLE XTMP_MENSAJE(
   PAIS VARCHAR(10),
   MENSAJE VARCHAR(200));
   
      
   CREATE TABLE XTMP_FLAGALERTA AS(
   SELECT 
   U.PAIS AS PAIS,
   A.PERIODO AS DIA,
   SYSDATE AS FECHA, 
   CASE  
   WHEN  TO_CHAR(TO_DATE(SYSDATE,'DD/MM/YYYY'),'DD') < A.PERIODO THEN 0
   WHEN  TO_CHAR(TO_DATE(SYSDATE,'DD/MM/YYYY'),'DD') = A.PERIODO THEN 1
   WHEN  TO_CHAR(TO_DATE(SYSDATE,'DD/MM/YYYY'),'DD') =  A.PERIODO+2 THEN 2
   WHEN  TO_CHAR(TO_DATE(SYSDATE,'DD/MM/YYYY'),'DD') >= A.PERIODO+4 THEN 3
   ELSE 0 
   END AS FLAG_ALERTA
   FROM DCS_USUARIO U
   RIGHT JOIN DCS_ALERTA A
   ON U.PK_USUARIO=A.ID_USUARIO AND U.PAIS IN (
   SELECT DISTINCT GV_PAIS
   FROM DIM_PAISES D
   WHERE NOT GV_PAIS IN
  (SELECT DISTINCT PAIS FROM XINDDL_ST WHERE PERIODO=TO_CHAR(SYSDATE,'YYYYMM')))
  );
   COMMIT;


/************ INSERTA REGISTROS DE CORREOS POR PAIS ********************/   
   
   INSERT INTO XTMP_MAIL
   SELECT U.PAIS,U.MAIL
   FROM DCS_USUARIO U
   WHERE  U.PAIS IN(SELECT F.PAIS FROM XTMP_FLAGALERTA F WHERE F.FLAG_ALERTA IN(1,2,3))
   AND NOT U.MAIL IS NULL;
   COMMIT;
  
   INSERT INTO XTMP_MAIL
   SELECT F.PAIS,JEFE1_CORREO AS CORREO FROM 
   DCS_USUARIO U
   LEFT JOIN DCS_ALERTA A
   ON ID_USUARIO=PK_USUARIO
   LEFT JOIN XTMP_FLAGALERTA F
   ON U.PAIS=F.PAIS
   WHERE F.FLAG_ALERTA IN(1,2,3) AND NOT JEFE1_CORREO IS NULL;
   COMMIT;

   INSERT INTO XTMP_MAIL
   SELECT F.PAIS,JEFE2_CORREO AS CORREO FROM 
   DCS_USUARIO U
   LEFT JOIN DCS_ALERTA A
   ON ID_USUARIO=PK_USUARIO
   LEFT JOIN XTMP_FLAGALERTA F
   ON U.PAIS=F.PAIS
   WHERE F.FLAG_ALERTA IN(2,3) AND NOT JEFE2_CORREO IS NULL;
   COMMIT;
   
   
   INSERT INTO XTMP_MAIL
   SELECT F.PAIS,JEFE3_CORREO AS CORREO FROM 
   DCS_USUARIO U
   LEFT JOIN DCS_ALERTA A
   ON ID_USUARIO=PK_USUARIO
   LEFT JOIN XTMP_FLAGALERTA F
   ON U.PAIS=F.PAIS
   WHERE F.FLAG_ALERTA =3 AND NOT JEFE3_CORREO IS NULL;
   COMMIT;

/************ INSERTA REGISTROS DE MENSAJES POR PAIS  ********************/  

   INSERT INTO XTMP_MENSAJE
   SELECT F.PAIS, MENSAJE FROM 
   DCS_USUARIO U
   LEFT JOIN DCS_ALERTA A
   ON ID_USUARIO=PK_USUARIO
   LEFT JOIN XTMP_FLAGALERTA F
   ON U.PAIS=F.PAIS
   WHERE F.FLAG_ALERTA IN(1,2,3) AND NOT MENSAJE IS NULL;
   COMMIT;
   

exit;