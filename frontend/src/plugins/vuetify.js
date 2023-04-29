import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

import HomeIcon from '@/common/component/icons/HomeIcon.vue'
import HomeOutlineIcon from '@/common/component/icons/HomeOutlineIcon.vue'
import RegisterIcon from '@/common/component/icons/RegisterIcon.vue'
import RegisterOutlineIcon from '@/common/component/icons/RegisterOutlineIcon.vue'
import MyPageIcon from '@/common/component/icons/MyPageIcon.vue'
import MyPageOutlineIcon from '@/common/component/icons/MyPageOutlineIcon.vue'
import ArrowLeftIcon from '@/common/component/icons/ArrowLeftIcon.vue'
import ArrowLeftWhiteIcon from '@/common/component/icons/ArrowLeftWhiteIcon.vue'
import DetailOutlineIcon from '@/common/component/icons/DetailOutlineIcon.vue'
import ShareOutlineIcon from '@/common/component/icons/ShareOutlineIcon.vue'
import CopyOutlineIcon from '@/common/component/icons/CopyOutlineIcon.vue'
import AddIcon from '@/common/component/icons/AddIcon.vue'
import CloseIcon from '@/common/component/icons/CloseIcon.vue'
import EditOutlineIcon from '@/common/component/icons/EditOutlineIcon.vue'
import UserInviteOutlineIcon from '@/common/component/icons/UserInviteOutlineIcon.vue'
import DeleteOutlineIcon from '@/common/component/icons/DeleteOutlineIcon.vue'
import LocationOutlineIcon from '@/common/component/icons/LocationOutlineIcon.vue'
import OutOutlineIcon from '@/common/component/icons/OutOutlineIcon.vue'
import FlagPlusIcon from '@/common/component/icons/FlagPlusIcon.vue'
import MenuIcon from '@/common/component/icons/MenuIcon.vue'
import CheckRoundIcon from '@/common/component/icons/CheckRoundIcon.vue'
import InfoOutlineIcon from '@/common/component/icons/InfoOutlineIcon.vue'
import MapOutlineIcon from '@/common/component/icons/MapOutlineIcon.vue'
import CameraOutlineIcon from '@/common/component/icons/CameraOutlineIcon.vue'
import ChattingOutlineIcon from '@/common/component/icons/ChattingOutlineIcon.vue'

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    values: {
      home: {
        component: HomeIcon
      },
      home_outline: {
        component: HomeOutlineIcon
      },
      register: {
        component: RegisterIcon
      },
      register_outline: {
        component: RegisterOutlineIcon
      },
      mypage: {
        component: MyPageIcon
      },
      mypage_outline: {
        component: MyPageOutlineIcon
      },
      arrow_left: {
        component: ArrowLeftIcon
      },
      arrow_left_white: {
        component: ArrowLeftWhiteIcon
      },
      detail_outline: {
        component: DetailOutlineIcon
      },
      share_outline: {
        component: ShareOutlineIcon
      },
      copy_outline: {
        component: CopyOutlineIcon
      },
      add: {
        component: AddIcon
      },
      close: {
        component: CloseIcon
      },
      edit_outline: {
        component: EditOutlineIcon
      },
      user_invite_outline: {
        component: UserInviteOutlineIcon
      },
      delete_outline: {
        component: DeleteOutlineIcon
      },
      location_outline: {
        component: LocationOutlineIcon
      },
      out_outline: {
        component: OutOutlineIcon
      },
      flag_plus: {
        component: FlagPlusIcon
      },
      menu: {
        component: MenuIcon
      },
      check_round: {
        component: CheckRoundIcon
      },
      info_outline: {
        component: InfoOutlineIcon
      },
      map_outline: {
        component: MapOutlineIcon
      },
      camera_outline: {
        component: CameraOutlineIcon
      },
      chatting_outline: {
        component: ChattingOutlineIcon
      }
    }
  }
});
