#include <stdio.h>
#include <string.h>

int lengthOfLongestSubstring(char* s) {
    int left = 0, right = 0;
    int max_len = 0;
    int char_set[256] = {0};  // ASCII ×Ö·û¼¯
    
    while (s[right] != '\0') {
        if (char_set[s[right]] == 0) {
            char_set[s[right]] = 1;
            right++;
            max_len = (right - left > max_len) ? (right - left) : max_len;
        } else {
            char_set[s[left]] = 0;
            left++;
        }
    }
    return max_len;
}

int main() {
    char s[] = "abcabcbb";
    printf("%d\n", lengthOfLongestSubstring(s));  // Êä³ö 3
    return 0;
}
