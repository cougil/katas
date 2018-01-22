package.path = package.path .. ";../?.lua"
require("common")

local c = coroutine.create(function()
  print("begin")
  print("sending: 1")
  coroutine.yield(1)
  print("sending: 2")
  coroutine.yield(2)
  print("end")
end)
_, r = coroutine.resume(c)
print("received: " .. to_s(r))
_, r = coroutine.resume(c)
print("received: " .. to_s(r))
_, r = coroutine.resume(c)
print("received: " .. to_s(r))