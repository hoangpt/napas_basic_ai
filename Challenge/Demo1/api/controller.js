
/**
 * Handles the calculation based on the provided query parameters.
 * 
 * @param {Object} req - The request object.
 * @param {Object} req.query - The query parameters.
 * @param {string} req.query.operation - The operation to perform (add, subtract, multiply, divide).
 * @param {string} req.query.operand1 - The first operand.
 * @param {string} req.query.operand2 - The second operand.
 * @param {Object} res - The response object.
 * 
 * @throws {Error} If the operation is unspecified.
 * @throws {Error} If operand1 is invalid.
 * @throws {Error} If operand2 is invalid.
 * @throws {Error} If the operation is invalid.
 * 
 * @returns {void}
 */
exports.calculate = function(req, res) {
  req.app.use(function(err, _req, res, next) {
    if (res.headersSent) {
      return next(err);
    }

    res.status(400);
    res.json({ error: err.message });
  });

  if (!req.query.operation) {
    throw new Error("Unspecified operation");
  }

  if (!req.query.operand1 ||
      !req.query.operand1.match(/^(-)?[0-9\.]+(e(-)?[0-9]+)?$/) ||
      req.query.operand1.replace(/[-0-9e]/g, '').length > 1) {
    throw new Error("Invalid operand1: " + req.query.operand1);
  }

  if (!req.query.operand2 ||
      !req.query.operand2.match(/^(-)?[0-9\.]+(e(-)?[0-9]+)?$/) ||
      req.query.operand2.replace(/[-0-9e]/g, '').length > 1) {
    throw new Error("Invalid operand2: " + req.query.operand2);
  }

    // TODO: Add operator
    var operations = {
      'add':      function(a, b) { return Number(a) + Number(b) },
      'subtract': function(a, b) { return Number(a) - Number(b) },
      'multiply': function(a, b) { return Number(a) * Number(b) },
      'divide':   function(a, b) { 
        if (Number(b) === 0) {
          throw new Error("Division by zero is not allowed");
        }
        return Number(a) / Number(b); 
      }
    };

    var operation = operations[req.query.operation];
    if (!operation) {
      throw new Error("Invalid operation: " + req.query.operation);
    }


  res.json({ result: operation(req.query.operand1, req.query.operand2) });
};


