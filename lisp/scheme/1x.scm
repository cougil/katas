(define (cc l)
  (define resumed-from #f)
  (define yield-from #f)
  (set! r (lambda (k)
    (l (lambda ()
      (set! k (call/cc k))
    ))
  ))
  (set! y (lambda (other)
    
  ))
  (lambda () (r y))
)

(define (main args)
  (display 0)
  (set! c (cc (lambda (yield)
    (display 1)
    (yield)
    ;(display 3)
  )))
  (c)
  (display 2)
  ;(resume)
  ;(display 5)
)
