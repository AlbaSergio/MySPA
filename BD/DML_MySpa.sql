SELECT H1.* FROM horario H1
	LEFT JOIN
(SELECT H.*
	FROM horario H
	INNER JOIN reservacion R
	ON H.idHorario = R.idHorario
	WHERE R.idSala = 3 AND R.fecha = STR_TO_DATE('01-01-2020', '%D-%M-%Y')) AS SQ2
ON H1.idHorario = SQ2.idHorario
WHERE SQ2.idHorario IS NULL;
