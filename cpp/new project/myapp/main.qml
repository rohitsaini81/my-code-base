import QtQuick 2.15
import QtQuick.Window 2.15
import QtQuick.Controls 2.15
import com.example.functions 1.0  // Import the C++ class

Window {
    width: 640
    height: 480
    visible: true
    title: qsTr("Hello World")


    MyFunctions {
        id: myFunctions  // Create an instance of the C++ class
    }

    Text {
        id: displayText
        anchors.horizontalCenter: parent.top
        text: "Hello, World!"
        font.pixelSize: 24
        color: "blue"
    }
    Button {
        id:te
        text: "Titan Engine"
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: displayText.bottom
                height: 40           // Explicitly set height
                width: 100           // Explicitly set width
                padding: 5           // Remove or reduce padding
                implicitWidth: 100   // Set width explicitly
                implicitHeight: 20   // Set height explicitly


        // onClicked: {
        //         if (displayText.text === "Hello, World!") {
        //             displayText.text = "Button Clicked!";
        //         } else {
        //             displayText.text = "Hello, World!";
        //         }
        // }

        onClicked: {
                    // Call the C++ function on button click
                    myFunctions.onButtonClick("Desktop/Work/Titan/Titan Editor",true);
                }

    }





    Button {
        id:ii
        text: "Intellije Ide!"
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: te.bottom
                height: 40           // Explicitly set height
                width: 100           // Explicitly set width
                padding: 5           // Remove or reduce padding
                implicitWidth: 100   // Set width explicitly
                implicitHeight: 20   // Set height explicitly


        onClicked: {
            myFunctions.onButtonClick("idea-IC-241.18034.62/bin/idea.sh",true);
                if (displayText.text === "Hello, World!") {
                    displayText.text = "Intellije Ide!";
                } else {
                    displayText.text = "Hello, World!";
                }
        }
    }

    Button {
        id:ci
        text: "Clion Ide!"
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: ii.bottom
                height: 40           // Explicitly set height
                width: 100           // Explicitly set width
                padding: 5           // Remove or reduce padding
                implicitWidth: 100   // Set width explicitly
                implicitHeight: 20   // Set height explicitly


        onClicked: {
            myFunctions.onButtonClick("clion-2024.2.3/bin/clion",true);
                if (displayText.text === "Hello, World!") {
                    displayText.text = "Clion Ide!";
                } else {
                    displayText.text = "Hello, World!";
                }
        }
    }
    Button {
        id:as
        text: "Android Studio !"
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: ci.bottom
                height: 40           // Explicitly set height
                width: 100           // Explicitly set width
                padding: 5           // Remove or reduce padding
                implicitWidth: 100   // Set width explicitly
                implicitHeight: 20   // Set height explicitly


        onClicked: {
            myFunctions.onButtonClick("android-studio/bin/studio.sh",true);
                if (displayText.text === "Hello, World!") {
                    displayText.text = "Android Studio !";
                } else {
                    displayText.text = "Hello, World!";
                }
        }
    }

    Button {
        id:bs
        text: "Blender Studio !"
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: as.bottom
                height: 40           // Explicitly set height
                width: 100           // Explicitly set width
                padding: 5           // Remove or reduce padding
                implicitWidth: 100   // Set width explicitly
                implicitHeight: 20   // Set height explicitly


        // onClicked: {
        //         if (displayText.text === "Hello, World!") {
        //             displayText.text = "Blender Studio !";
        //         } else {
        //             displayText.text = "Hello, World!";
        //         }
        // }

        onClicked: {
            // Call the C++ function on button click
            myFunctions.onButtonClick("blender-4.2.1-linux-x64/blender",true);
        }
    }

    Button {
        id:ida
        text: "IDA Freeware 8.4. !"
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: bs.bottom
                height: 40           // Explicitly set height
                width: 100           // Explicitly set width
                padding: 5           // Remove or reduce padding
                implicitWidth: 100   // Set width explicitly
                implicitHeight: 20   // Set height explicitly


        onClicked: {
            myFunctions.onButtonClick("idafree-8.4/ida64",true);
                if (displayText.text === "Hello, World!") {
                    displayText.text = "DA Freeware 8.4. !";
                } else {
                    displayText.text = "Hello, World!";
                }
        }
    }

    Button {
        id:jm
        text: "JMonkey!"
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: ida.bottom
                height: 40           // Explicitly set height
                width: 100           // Explicitly set width
                padding: 5           // Remove or reduce padding
                implicitWidth: 100   // Set width explicitly
                implicitHeight: 20   // Set height explicitly


        onClicked: {
            myFunctions.onButtonClick("jmonkeyplatform/bin/jmonkeyplatform",true);

                if (displayText.text === "Hello, World!") {
                    displayText.text = "JMonkey!";
                } else {
                    displayText.text = "Hello, World!";
                }
        }
    }


    Button {
        id:nv
        text: "NeoVide!"
        anchors.horizontalCenter: parent.horizontalCenter
        anchors.top: jm.bottom
        height: 40           // Explicitly set height
        width: 100           // Explicitly set width
        padding: 5           // Remove or reduce padding
        implicitWidth: 100   // Set width explicitly
        implicitHeight: 20   // Set height explicitly


        // onClicked: {
        //     if (displayText.text === "Hello, World!") {
        //         displayText.text = "NeoVide !";
        //     } else {
        //         displayText.text = "Hello, World!";
        //     }
        // }

        onClicked: {
            myFunctions.onButtonClick("neovide",false);
        }
    }


    Button {
        id:code
        text: "Visual Studio Code !"
                anchors.horizontalCenter: parent.horizontalCenter
                anchors.top: nv.bottom
                height: 40           // Explicitly set height
                width: 100           // Explicitly set width
                padding: 5           // Remove or reduce padding
                implicitWidth: 100   // Set width explicitly
                implicitHeight: 20   // Set height explicitly


        onClicked: {
            myFunctions.onButtonClick("code",false);
                if (displayText.text === "Hello, World!") {
                    displayText.text = "Visual Studio Code !";
                } else {
                    displayText.text = "Hello, World!";
                }
        }

    }

}
