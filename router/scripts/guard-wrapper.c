#include <stdio.h>
#include <unistd.h>

int main(int argc, char** argv) {
    printf("%d", geteuid());
    return 0;
}