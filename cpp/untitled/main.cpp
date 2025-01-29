#include <QCoreApplication>
#include <QTimer>
#include <iostream>
#include <QtLogging>
// #include <QApplication>
// #include <QLabel>

int main(int argc, char *argv[])
{
    QCoreApplication a(argc, argv);

    std::cout<<"Hello World";
    // log(100);
    // QTimer::singleShot(5000,&a,&QCoreApplication::quit);
    return a.exec();
}


