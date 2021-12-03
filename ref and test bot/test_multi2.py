import os
from multiprocessing import Process

num = 42


def f(name):
    global num
    num += 1
    print('pid of parent:', os.getppid())
    print('pid of %s : %d' % (name, os.getpid()))
    print('%d' % num)


if __name__ == '__main__':
    print('pid of main:', os.getpid())

    p1 = Process(target=f, args=("proc_1",))
    p2 = Process(target=f, args=("proc_2",))

    p1.start();
    p1.join()
    p2.start();
    p2.join()