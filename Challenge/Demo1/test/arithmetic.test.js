require('./helpers');

describe('Arithmetic', function () {
    // validation test
    describe('Validation', function () {
        it('rejects missing operation', function (done) {
            request.get('/arithmetic?operand1=21&operand2=21')
                .expect(400)
                .end(function (err, res) {
                    expect(res.body).to.eql({error: "Unspecified operation"});
                    done();
                });
        });
        it('rejects invalid operation', function (done) {
            request.get('/arithmetic?operation=foobar&operand1=21&operand2=21')
                .expect(400)
                .end(function (err, res) {
                    expect(res.body).to.eql({error: "Invalid operation: foobar"});
                    done();
                });
        });
        it('rejects missing operand1', function (done) {
            request.get('/arithmetic?operation=add&operand2=21')
                .expect(400)
                .end(function (err, res) {
                    expect(res.body).to.eql({error: "Invalid operand1: undefined"});
                    done();
                });
        });
        it('rejects operands with invalid sign', function (done) {
            request.get('/arithmetic?operation=add&operand1=4.2-1&operand2=4')
                .expect(400)
                .end(function (err, res) {
                    expect(res.body).to.eql({error: "Invalid operand1: 4.2-1"});
                    done();
                });
        });
        it('rejects operands with invalid decimals', function (done) {
            request.get('/arithmetic?operation=add&operand1=4.2.1&operand2=4')
                .expect(400)
                .end(function (err, res) {
                    expect(res.body).to.eql({error: "Invalid operand1: 4.2.1"});
                    done();
                });
        });
    });

    // add test
    describe('Addition', function () {
        it('adds two positive integers', function (done) {
            request.get('/arithmetic?operation=add&operand1=21&operand2=21')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 42});
                    done();
                });
        });
        it('adds zero to an integer', function (done) {
            request.get('/arithmetic?operation=add&operand1=42&operand2=0')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 42});
                    done();
                });
        });
        it('adds a negative integer to a positive integer', function (done) {
            request.get('/arithmetic?operation=add&operand1=21&operand2=-42')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: -21});
                    done();
                });
        });
        it('adds two negative integers', function (done) {
            request.get('/arithmetic?operation=add&operand1=-21&operand2=-21')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: -42});
                    done();
                });
        });
        it('adds an integer to a floating point number', function (done) {
            request.get('/arithmetic?operation=add&operand1=2.5&operand2=-5')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: -2.5});
                    done();
                });
        });
        it('adds with negative exponent', function (done) {
            request.get('/arithmetic?operation=add&operand1=1.2e-5&operand2=-1.2e-5')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 0});
                    done();
                });
        });
    });

    // subtract test
    describe('Subtraction', function () {
        it('subtracts two positive integers', function (done) {
            request.get('/arithmetic?operation=subtract&operand1=21&operand2=21')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 0});
                    done();
                });
        });
        it('subtracts zero from an integer', function (done) {
            request.get('/arithmetic?operation=subtract&operand1=42&operand2=0')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 42});
                    done();
                });
        });
        it('subtracts a negative integer from a positive integer', function (done) {
            request.get('/arithmetic?operation=subtract&operand1=21&operand2=-42')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 63});
                    done();
                });
        });
        it('subtracts two negative integers', function (done) {
            request.get('/arithmetic?operation=subtract&operand1=-21&operand2=-21')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 0});
                    done();
                });
        });
        it('subtracts an integer from a floating point number', function (done) {
            request.get('/arithmetic?operation=subtract&operand1=2.5&operand2=-5')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 7.5});
                    done();
                });
        });
        it('subtracts with negative exponent', function (done) {
            request.get('/arithmetic?operation=subtract&operand1=1.2e-5&operand2=-1.2e-5')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 2.4e-5});
                    done();
                });
        });
    });

    // divide test
    describe('Division', function () {
        it('divides two positive integers', function (done) {
            request.get('/arithmetic?operation=divide&operand1=21&operand2=21')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 1});
                    done();
                });
        });
        it('divides an integer by zero', function (done) {
            request.get('/arithmetic?operation=divide&operand1=42&operand2=0')
                .expect(400)
                .end(function (err, res) {
                    expect(res.body).to.eql({error: "Division by zero is not allowed"});
                    done();
                });
        });
        it('divides a negative integer by a positive integer', function (done) {
            request.get('/arithmetic?operation=divide&operand1=-21&operand2=7')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: -3});
                    done();
                });
        });
        it('divides two negative integers', function (done) {
            request.get('/arithmetic?operation=divide&operand1=-21&operand2=-7')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 3});
                    done();
                });
        });
        it('divides an integer by a floating point number', function (done) {
            request.get('/arithmetic?operation=divide&operand1=5&operand2=2.5')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 2});
                    done();
                });
        });
        it('divides with negative exponent', function (done) {
            request.get('/arithmetic?operation=divide&operand1=1.2e-5&operand2=-1.2e-5')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: -1});
                    done();
                });
        });

        // division equal to floating point number
        it('divides two floating point numbers', function (done) {
            request.get('/arithmetic?operation=divide&operand1=8&operand2=3')
                .expect(200)
                .end(function (err, res) {
                    expect(res.body).to.eql({result: 2.667});
                    done();
                });
        });
    });
});
