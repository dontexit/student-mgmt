#! bin/bash

if [ -z "$MODULEPATH" ]; then
				echo "MODULEPATH is not set, set MODULEPATH before running the script again."
else
				javac --module-path "$MODULEPATH" --add-modules javafx.controls -d bin src/StudentAdmin.java # StudentAdmin is the name of our entry class / file  

				
				java --module-path "$MODULEPATH" --add-modules javafx.controls -cp bin StudentAdmin # StudentAdmin is the name of our entry class / file  
fi


