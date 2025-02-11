#ifndef MYFUNCTIONS_H
#define MYFUNCTIONS_H

#include <QObject>
#include <QString>
#include <QDebug>


#include <QProcess>

class MyFunctions : public QObject
{
    Q_OBJECT

    public:QProcess process;
    // public:QString command = "pwd";
    public:QString command = "/home/rohit/";


public:
    explicit MyFunctions(QObject *parent = nullptr) : QObject(parent),counter(0) {}

    Q_INVOKABLE void onButtonClick(QString App, bool isPath) {

      if (isPath) {
              command = command + App;
      }else{command = App; }
        process.start(command);
        process.waitForFinished();
        QByteArray output = process.readAllStandardOutput();
        qDebug() << "Command Output:" << output;

        counter+=1;
        qDebug() << "Button clicked! "<<counter;
    }

private:
    int counter;
};

#endif // MYFUNCTIONS_H
