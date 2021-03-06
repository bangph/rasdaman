// This query tests different selectivities a query box starting at
// the origin on tomo_cubed.

// [0.5] cubed, selectivity 0,5%

select img[0:44,0:44,0:26]
from tomo_cubed as img

// [1] cubed, selectivity 1%

select img[0:55,0:55,0:33]
from tomo_cubed as img

// [2] cubed, selectivity 2%

select img[0:69,0:69,0:42]
from tomo_cubed as img

// [5] cubed, selectivity 5%

select img[0:94,0:94,0:56]
from tomo_cubed as img

// [10] cubed, selectivity 10%

select img[0:118,0:118,0:71]
from tomo_cubed as img

// [20] cubed, selectivity 0%

select img[0:149,0:149,0:89]
from tomo_cubed as img

// [50] cubed, selectivity 50%

select img[0:202,0:202,0:121]
from tomo_cubed as img

// [100] cubed, selectivity 100%

select img[0:255,0:255,0:153]
from tomo_cubed as img

