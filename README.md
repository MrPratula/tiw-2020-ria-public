# tiw-2020-ria
## Esercizio 3: gestione documenti
#### Versione pure HTML

Un’applicazione web consente la gestione di cartelle, sottocartelle e documenti online. Una cartella ha un nome e una data di creazione e può contenere (solo) sottocartelle. Una sottocartella può contenere (solo) dei documenti. Un documento ha un nome, una data di creazione, un sommario e un tipo. Quando l’utente accede all’applicazione appare una HOME PAGE che contiene un albero delle cartelle e delle sottocartelle.
Nell’HOME page l’utente può selezionare una sottocartella e accedere a una pagina DOCUMENTI che mostra l’elenco dei documenti di una sottocartella. Ogni documento in elenco ha due link: accedi e sposta. Quando l’utente seleziona il link accedi, appare una pagina DOCUMENTO che mostra tutti i dati del documento selezionato. Quando l’utente seleziona il link sposta, appare la HOME PAGE con l’albero delle cartelle e delle sottocartelle; in questo caso la pagina mostra il messaggio “Stai spostando il documento X dalla sottocartella Y. Scegli la sottocartella di destinazione”, la sottocartella a cui appartiene il documento da spostare NON è selezionabile e il suo nome è evidenziato (per esempio con un colore diverso). Quando l’utente seleziona la sottocartella di destinazione, il documento è spostato dalla sottocartella di origine a quella di destinazione e appare la pagina DOCUMENTI che mostra il contenuto aggiornato della sottocartella di destinazione.

#### Versione con JavaScript

Si realizzi un’applicazione client server web che modifica le specifiche precedenti come segue:

 - L’applicazione supporta registrazione e login mediante una pagina pubblica con opportune form. La registrazione controlla la validità sintattica dell’indirizzo di email e l’uguaglianza tra i campi “password” e “ripeti password”, anche a lato client. La registrazione controlla l’unicità dello username;
 - Dopo il login, l’intera applicazione è realizzata con un’unica pagina;
 - Ogni interazione dell’utente è gestita senza ricaricare completamente la pagina, ma produce l’invocazione asincrona del server e l’eventuale modifica del contenuto da aggiornare a seguito dell’evento;
 - La funzione di spostamento di un documento è realizzata mediante drag and drop;
 - Si aggiunge una cartella denominata “cestino”. Il drag & drop di un documento o di una cartella nel cestino comporta la cancellazione. Prima di inviare il comando di cancellazione al server l’utente vede una finestra modale di conferma e può decidere se annullare l’operazione o procedere.
