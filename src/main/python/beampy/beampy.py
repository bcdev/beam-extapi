class Operator:

    def __init__(self):
        pass



def isprime(n):
    for x in range(2, n):
        if n % x == 0:
            print(n, 'equals', x, '*', n//x)
            break
        else:
            # loop fell through without finding a factor
            print(n, 'is a prime number')



if __name__ == "__main__":
    print('This module cannot be run stand-alone.')