(function () {

    // page components
    let foldersList;
    let fileList;
    let lastSelectedSubFolderId;
    let activeFile;
    let currentSelectedFile;

    const pageOrchestrator = new PageOrchestrator();

    window.addEventListener("load", () => {
        if (sessionStorage.getItem("user") == null) {
            window.location.href = "index.html"
        } else {
            pageOrchestrator.start();
            pageOrchestrator.refresh();
        }
    })

    function PageOrchestrator() {

        const alertContainer = document.getElementById("idAlert");

        this.start = function () {
            let userFullName = new PersonalMessage(sessionStorage.getItem("user"), document.getElementById("userFullName"));
            userFullName.show();

            foldersList = new ShowFolders(localStorage.getItem("folders"), document.getElementById("foldersDiv"));

            initTrashCan();
        }

        this.refresh = function () {
            alertContainer.textContent ="";
        };
    }

    function PersonalMessage (user, messageContainer) {

        const userObj = JSON.parse(user);

        const name = userObj.name;
        const surname = userObj.surname;

        this.userFullName = name + ' ' + surname;
        this.show = function () {
            messageContainer.textContent = this.userFullName;
        }
    }

    function ShowFolders(folders, div) {

        const foldersObj = JSON.parse(folders);
        let counter;
        const foldersNumber = foldersObj.length - 1;

        // cycle to get external folder
        for (counter=0; counter<=foldersNumber;counter++) {

            const ul = document.createElement("ul");
            const li = document.createElement("li");
            const folder = foldersObj[counter];

            li.textContent = folder.name;
            ul.appendChild(li);
            div.appendChild(ul);

            const subFolders = folder.subFolders;
            const subFoldersNumber = subFolders.length - 1;
            let counter2;

            // 2nd cycle to get sub folders
            for (counter2=0; counter2<=subFoldersNumber; counter2++){

                const ul2 = document.createElement("ul");
                const li2 = document.createElement("li");

                // the li2 doesn't have a text, has a <a> with a text and a link and parameters
                const subFolder = subFolders[counter2];
                ul2.appendChild(li2);
                ul.appendChild(ul2);

                const anchor = document.createElement("a");
                li2.appendChild(anchor);
                li2.setAttribute("id", subFolder.id);
                anchor.setAttribute("subFolderId", subFolder.id);

                anchor.textContent = subFolder.name;

                // click opens sub folders files
                anchor.addEventListener("click", (ev) => {

                    const subFolderId = ev.target.getAttribute("subFolderId");

                    // show all files on click
                    fileList = new ShowFiles(document.getElementById("filesDiv"), subFolderId);

                    // reset the blue mark
                    if (lastSelectedSubFolderId === undefined) {

                    } else {
                        document.getElementById(lastSelectedSubFolderId).classList.remove("mark");
                    }

                    // if open, close active file view
                    document.getElementById("fileDiv").classList.add("masked");
                    document.getElementById("trash").classList.remove("masked");

                    // update the last selected sub folder
                    lastSelectedSubFolderId = subFolderId;
                    document.getElementById(lastSelectedSubFolderId).classList.add("mark");

                });

                // a file can be dropped on a sub folder
                anchor.addEventListener("dragover", (ev) => {
                    ev.preventDefault();
                });

                // if a file is dropped here it has to be moved here
                anchor.addEventListener("drop", (ev) => {

                    ev.preventDefault();

                    const cantDropHere = lastSelectedSubFolderId;
                    const fileToMove = currentSelectedFile;
                    const targetFolder = ev.target.getAttribute("subFolderId");

                    if (targetFolder !== cantDropHere){

                        callServlet("GET", "MoveFile?fileId="+fileToMove+"&destination="+targetFolder, null, function (request) {

                            if (request.readyState === 4){
                                if(request.status === 200){
                                    alert("Operation complete!");
                                }
                            }
                            pageOrchestrator.refresh();
                        });

                    } else
                        alert("This file is already in that folder!");
                })

                anchor.setAttribute("subFolderId", subFolder.id);

                anchor.href = "#"+subFolder.name;
            }
        }
    }

    function ShowFiles(filesDiv, subfolderId) {

       callServlet("GET", "folderView?subFolderId="+subfolderId, null, function (request) {

           if(request.readyState === 4){
               if(request.status === 200) {
                   fileList = JSON.parse(request.responseText);

                   // empty the older div if it exist
                   filesDiv.innerHTML = "";

                   let counter;
                   const filesNumber = fileList.length - 1;

                   for (counter=0; counter<=filesNumber;counter++) {

                       const ul = document.createElement("ul");
                       const li = document.createElement("li");
                       const anchor = document.createElement("a");
                       const file = fileList[counter];
                       const fileName = file.name + '.' + file.extension;

                       anchor.setAttribute("fileId", file.id);
                       anchor.setAttribute("fileFullName", fileName);
                       anchor.setAttribute("draggable", "true");
                       anchor.textContent = file.name;
                       anchor.textContent = fileName;
                       anchor.href = "#"+file.name;

                       // click opens file info
                       anchor.addEventListener("click", (ev) => {

                           const fileId = ev.target.getAttribute("fileId");

                           // show target file on click
                           activeFile = new OpenFile(document.getElementById("fileDiv"), fileId);

                       });

                       // anchor has drag option on start display name of file to drag
                       anchor.addEventListener("dragstart", (ev) =>{

                           const draggingFile = ev.target.getAttribute("fileFullName");
                           const messageBox = document.getElementById("fileMessage");

                           messageBox.classList.remove("masked");
                           messageBox.textContent = "Drag " + draggingFile + " on the sub folder you want to move it!";

                           currentSelectedFile = ev.target.getAttribute("fileId");
                       });

                       // when the file is dropped the name is hidden
                       anchor.addEventListener("dragend", () => {
                           const messageBox = document.getElementById("fileMessage");
                           messageBox.classList.add("masked");
                       })

                       // link the anchor to the li and ul
                       li.appendChild(anchor);
                       ul.appendChild(li);
                       filesDiv.appendChild(ul);

                   }
               }
           }
       })
    }

    function OpenFile(openFileDiv, fileId) {

        callServlet("GET", "fileView?fileId="+fileId, null, function (request) {

            if (request.readyState === 4){
                if (request.status === 200) {
                    activeFile = JSON.parse(request.responseText);

                    document.getElementById("tableName").textContent = activeFile.name;
                    document.getElementById("tableExtension").textContent = activeFile.extension;
                    document.getElementById("tableDate").textContent = activeFile.creationDate;
                    document.getElementById("tableID").textContent = activeFile.id;
                    document.getElementById("tableLocation").textContent = activeFile.owner;
                    document.getElementById("fileSummary").textContent = activeFile.summary;

                    openFileDiv.classList.remove("masked")
                }
            }
        });
    }

    // file can be dropped on trash can
    function initTrashCan() {

        // show element can be dropped here
        document.getElementById("trash").addEventListener("dragover", (ev) => {
            ev.preventDefault();
        })

        // handle the deletion of the file dropped
        document.getElementById("trash").addEventListener("drop", () => {

            const answer = confirm("This action can not be undone!\nDo you want to proceed?");

            if (answer === true) {

                // on a draggable item the source and the target are both the same image (dunno why)

                callServlet("GET", "fileDeletion?fileId="+currentSelectedFile, null, function (request) {

                    if (request.readyState === 4) {
                        if (request.status === 200) {
                            alert("File successfully delete!");
                        }
                        pageOrchestrator.refresh();
                    }
                });
            }
        });
    }

})();