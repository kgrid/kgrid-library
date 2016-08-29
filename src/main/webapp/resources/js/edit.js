$(document).ready(
function enableEdit() {
            document.getElementById("editButton").style.display = "none";
            document.getElementById("saveButton").style.display = "inline-block";
             document.getElementById("cancelButton").style.display = "inline-block";
            var y = document.getElementsByClassName("metaEdit");
            var i;
            for (i = 0; i < y.length; i++) {
                 y[i].disabled = false;
                }          
         })

$(document).ready(		function disableEdit() {
            document.getElementById("editButton").style.display = "inline-block";
                       document.getElementById("saveButton").style.display = "none";
             document.getElementById("cancelButton").style.display = "none";
            var y = document.getElementsByClassName("metaEdit");
            var i;
            for (i = 0; i < y.length; i++) {
                 y[i].disabled = true;
                }          
         })

$(document).ready(
		function savechange() {
              document.getElementById("editButton").style.display = "inline-block";
                       document.getElementById("saveButton").style.display = "none";
             document.getElementById("cancelButton").style.display = "none";          
             var y = document.getElementsByClassName("metaEdit");
            var i;
            for (i = 0; i < y.length; i++) {
                 y[i].disabled = true;
                }          
            alert("Changes will be saved.");
            /* Need code to save the changes */
            
            
            
         })
