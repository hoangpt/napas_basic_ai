const { expect } = require('chai');
const { hasIncrementalDigits } = require('../../api/controller/utils/number');

describe('hasIncrementalDigits', function () {
    it('should return true for a number with incremental digits', function () {
        expect(hasIncrementalDigits(123456)).to.be.true;
    });

    it('should return false for a number without incremental digits', function () {
        expect(hasIncrementalDigits(123457)).to.be.false;
    });

    it('should return false for a number that is too short', function () {
        expect(hasIncrementalDigits(1234)).to.be.false;
    });

    it('should return false for a number with incremental digits but shorter than 5 digits', function () {
        expect(hasIncrementalDigits(12345)).to.be.false;
    });

    it('should return false for a number with non-incremental digits', function () {
        expect(hasIncrementalDigits(13579)).to.be.false;
    });

    it('should return true for a number with incremental digits in the middle', function () {
        expect(hasIncrementalDigits(912345678)).to.be.true;
    });

    it('should return true for a number with incremental digits in the middle', function () {
        expect(hasIncrementalDigits('          ')).to.be.true;
    });    
});