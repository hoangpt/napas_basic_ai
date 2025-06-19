/**
 * Converts the given number to a string.
 * 
 * @constant {string} numStr - The string representation of the number.
 * @param {number} number - The number to check for incremental digits.
 * 
 * Example usage:
 *  123456 -> true
 *  123457 -> false
 *  1234 -> false 
 * 
 * @returns {boolean} - Returns true if the number has incremental digits, otherwise false.
 */
exports.hasIncrementalDigits = function(number) {
    const numStr = number.toString();
  
    // trim all spaces
    numStr = numStr.replace(/\s/g, '');
  
    if (numStr.length <= 5) {
      return false;
    }
  
    for (let i = 0; i < numStr.length - 1; i++) {
      if (parseInt(numStr[i]) + 1 !== parseInt(numStr[i + 1])) {
        return false;
      }
    }
    return true;
  }