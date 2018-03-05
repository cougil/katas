(define (log message)
  (display message)
  (newline)
)

(define (f)
  (set! yield (lambda ()
    (call/cc (lambda (continuation)
      (set! run-procedure (lambda () (continuation #f)))
      (jump-out)
  ))))

  (set! resume (lambda ()
    (call/cc (lambda (continuation)
      (set! jump-out (lambda () (continuation #f)))
      (run-procedure)
  ))))

  (set! run-procedure (lambda ()
    (log 2)
    (yield)
    (log 4)
    (jump-out)
  ))
)

(define (main args)
  (define count 0)
  (f)
  (log 1)
  (resume)
  (log 3)
  (cond ((< count 3) (begin
    (set! count (+ 1 count))
    (log "🚀")
    (resume)
  )))
)
