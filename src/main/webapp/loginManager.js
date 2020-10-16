(function () {

    // show and hide registration form
    const showRegistrationButton = document.getElementById("showRegistrationButton");
    const showLogInButton = document.getElementById("showLogInButton");

    showRegistrationButton.addEventListener("click", () => {
        const registrationForm = document.getElementById("registrationForm");
        const logInForm = document.getElementById("logInForm");
        registrationForm.classList.add("visible");
        logInForm.classList.add("masked");
    });

    showLogInButton.addEventListener("click", ()=>{
        const registrationForm = document.getElementById("registrationForm");
        const logInForm = document.getElementById("logInForm");
        registrationForm.classList.remove("visible");
        logInForm.classList.remove("masked");
    });

    // log in submit

    document.getElementById("loginButton").addEventListener("click", (ev) => {

        const form = ev.target.closest("form");
        if (form.checkValidity()) {

            // call the servlet
            callServlet("POST", "Home", form,function (request) {

                if (request.readyState === XMLHttpRequest.DONE) {

                    // message contains all and need to be divided

                    const message = request.responseText;
                    const messageArray = message.split("\n");
                    const user = JSON.parse(messageArray[0]);
                    const folders = JSON.parse(messageArray[1]);

                    if (user === "Account not existing") {
                        document.getElementById("loginError").textContent = user;
                        return;
                    }

                    switch (request.status) {

                        // ok
                        case 200: {
                            sessionStorage.setItem("user", JSON.stringify(user));
                            window.localStorage.setItem("folders", JSON.stringify(folders));
                            window.location.href = "home.html";
                            break;
                        }

                        case 400:           // bad request
                        case 401:           // unauthorized
                        case 500: {         // server error
                            document.getElementById("loginError").textContent = message;
                            break;
                        }
                    }
                }
            });
        } else {
            form.reportValidity();
        }
    });


    // registration submit

    document.getElementById("registerButton").addEventListener("click", (ev) => {

        const form = ev.target.closest("form");

        const password = form.querySelector("[name=password]").value;
        const confirmPassword = form.querySelector("[name=confirmPassword]").value;
        const mail = form.querySelector("[name=email]").value;


        console.log(form.checkValidity);
        console.log(isAGoodMail(mail));
        console.log(samePassword(password, confirmPassword));


        if (form.checkValidity() && isAGoodMail(mail) && samePassword(password, confirmPassword)) {

            callServlet("POST", "Register", form, function (request) {

                if (request.readyState === XMLHttpRequest.DONE) {
                    if(request.status === 200) {

                        alert("Your account has been created!\nLog in!");
                        window.location.href = "index.html";
                    } else {
                        alert(request.responseText);
                    }
                }
            });
        }
    });

    function isAGoodMail(mail) {

        var mailPattern = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

        if(mail.match(mailPattern))
            return true;
        else {
            alert("This Mail address is not valid!");
            return false;
        }
    }

    function samePassword (password, confirmPassword) {

        if (password == null || confirmPassword == null) {
            alert("Password can not be empty!");
            return false;
        }

        if (!(password  === confirmPassword)){
            alert("The password must be equal!");
            return password === confirmPassword;
        }
        return true;
    }
})();


/*		Explanation:


 		/ .. /	All regular expressions start and end with forward slashes.

		^	Matches the beginning of the string or line.

		\w+	Matches one or more word characters including the underscore. (There must be at least one character before the '@')

		[\.-]	\ Indicates that the next character is special and not to be interpreted literally.

				.- matches character . or -.

		?	Matches the previous character 0 or 1 time. Here previous character is [.-].

		\w+	Matches 1 or more word characters including the underscore.

		*	Matches the previous character 0 or more times.

		([.-]?\w+)*	Matches 0 or more occurrences of [.-]?\w+.



		\w+([.-]?\w+)*	This sub-expression is used to match the username in the email. It begins with at least one or more word characters including the underscore,
		followed by . or -  and  . or - must follow by a word character (A-Za-z0-9_).

		@	It matches only @ character.

		\w+([.-]?\w+)*	It matches the domain name with the same pattern of user name described above..

		\.\w{2,3}	It matches a . followed by two or three word characters, (.com, .it)

		+	The + sign specifies that the above sub-expression shall occur one or more times

		$	Matches the end of the string or line.

 */